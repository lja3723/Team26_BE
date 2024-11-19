package org.ktc2.cokaen.wouldyouin.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktc2.cokaen.wouldyouin._common.util.KakaoPayUtil;
import org.ktc2.cokaen.wouldyouin._common.util.RestClientUtil;
import org.ktc2.cokaen.wouldyouin._common.util.UriUtil;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.event.application.EventService;
import org.ktc2.cokaen.wouldyouin.member.application.MemberService;
import org.ktc2.cokaen.wouldyouin.payment.dto.KakaoPayRequest;
import org.ktc2.cokaen.wouldyouin.payment.dto.KakaoPayResponse;
import org.ktc2.cokaen.wouldyouin.payment.dto.PayCompleteResponse;
import org.ktc2.cokaen.wouldyouin.payment.exception.FailedToPayException;
import org.ktc2.cokaen.wouldyouin.payment.persist.Payment;
import org.ktc2.cokaen.wouldyouin.payment.persist.PaymentRepository;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.KakaoPayReservationResponse;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.ReservationRequest;
import org.ktc2.cokaen.wouldyouin.reservation.application.ReservationService;
import org.ktc2.cokaen.wouldyouin.reservation.persist.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestClientUtil client;
    private final MemberService memberService;
    private final EventService eventService;
    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Value("${oauth.payment.kakao-pay-request-host}")
    private String kakaoPayRequestHost;
    @Value("${oauth.payment.kakao_pay_single_payment_url}")
    private String kakaoPaySinglePaymentUrl;
    @Value("${oauth.payment.approval_url}")
    private String approvalUrl;
    @Value("${oauth.payment.cancel_url}")
    private String cancelUrl;
    @Value("${oauth.payment.fail_url}")
    private String failUrl;
    @Value("${oauth.payment.secret_key}")
    private String secretKey;

    @Transactional
    public String readyPayment(MemberIdentifier identifier, ReservationRequest reservationRequest) {
        KakaoPayRequest kakaoPayRequest =
            KakaoPayRequest.of(
                identifier, eventService.getByIdOrThrow(reservationRequest.getEventId()),
                reservationRequest, approvalUrl, cancelUrl, failUrl
            );
        Payment payment = paymentRepository.save(kakaoPayRequest.toEntity(reservationRequest));
        paymentRepository.flush();
        log.debug("kakaoPayRequest: {}", kakaoPayRequest);
        log.debug("payment: {}", payment);
        KakaoPayResponse kakaoPayResponse = client.post(
            KakaoPayResponse.class,
            UriUtil.assembleFullUrl(kakaoPayRequestHost, kakaoPaySinglePaymentUrl),
            KakaoPayUtil.createKakaoPayRequestHeaders(kakaoPayRequestHost, secretKey),
            KakaoPayUtil.createKakaoPayRequestBody(payment.getPartnerOrderId(), kakaoPayRequest),
            (req, rsp) -> {
                throw new FailedToPayException("카카오페이 API 요청을 실패하였습니다.");
            }
        );
        payment.setTid(kakaoPayResponse.getTid());
        paymentRepository.save(payment);
        paymentRepository.flush();
        return kakaoPayResponse.getNextRedirectPcUrl() + "?orderId=" + payment.getPartnerOrderId();
    }

    @Transactional
    public String readyPaymentTest() {
        Payment payment = Payment.builder()
            .partnerOrderId(1L)
            .partnerUserId(10L)
            .quantity(1)
            .cid("TC0ONETIME")
            .eventId(1L)
            .itemName("아이템 이름")
            .build();

        KakaoPayRequest kakaoPayRequest = KakaoPayRequest.builder()
            .cid("TC0ONETIME")
            .partnerUserId("10")
            .itemName("상품명")
            .quantity("1")
            .totalAmount("1000000")
            .taxFreeAmount("0")
            .approvalUrl(approvalUrl)
            .cancelUrl(cancelUrl)
            .failUrl(failUrl)
            .build();

        KakaoPayResponse kakaoPayResponse = client.post(
            KakaoPayResponse.class,
            UriUtil.assembleFullUrl(kakaoPayRequestHost, kakaoPaySinglePaymentUrl),
            KakaoPayUtil.createKakaoPayRequestHeaders(kakaoPayRequestHost, secretKey),
            KakaoPayUtil.createKakaoPayRequestBody(payment.getPartnerOrderId(), kakaoPayRequest),
            (req, rsp) -> {
                throw new FailedToPayException("카카오페이 API 요청을 실패하였습니다.");
            }
        );
        payment.setTid(kakaoPayResponse.getTid());
        paymentRepository.save(payment);
        paymentRepository.flush();
        return kakaoPayResponse.getNextRedirectPcUrl() + "?orderId=" + payment.getPartnerOrderId();
    }

    @Transactional
    public Long approvePayment(Long orderId, String pgToken) {
        Payment payment = paymentRepository.findById(orderId)
            .orElseThrow(() -> new FailedToPayException("결제 정보를 찾을 수 없습니다."));
        client.post(
            Void.class,
            UriUtil.assembleFullUrl(kakaoPayRequestHost, "/online/v1/payment/approve"),
            KakaoPayUtil.createKakaoPayRequestHeaders(kakaoPayRequestHost, secretKey),
            KakaoPayUtil.createPayCompleteRequestBody(payment, pgToken),
            (req, rsp) -> {
                throw new FailedToPayException("카카오페이 결제 승인을 실패하였습니다.");
            }
        );
        ReservationRequest request = new ReservationRequest(payment.getEventId(), payment.getQuantity());
        reservationService.create(payment.getPartnerOrderId(), request);
        return payment.getPartnerUserId();
    }

    @Transactional
    public void approvePaymentTest(Long orderId, String pgToken) {
        Payment payment = paymentRepository.findById(orderId)
            .orElseThrow(() -> new FailedToPayException("결제 정보를 찾을 수 없습니다."));
        client.post(
            PayCompleteResponse.class,
            UriUtil.assembleFullUrl(kakaoPayRequestHost, "/online/v1/payment/approve"),
            KakaoPayUtil.createKakaoPayRequestHeaders(kakaoPayRequestHost, secretKey),
            KakaoPayUtil.createPayCompleteRequestBody(payment, pgToken),
            (req, rsp) -> {
                throw new FailedToPayException("카카오페이 결제 승인을 실패하였습니다.");
            }
        );
    }
}
