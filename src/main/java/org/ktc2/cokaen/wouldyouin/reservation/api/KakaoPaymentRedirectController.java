package org.ktc2.cokaen.wouldyouin.reservation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktc2.cokaen.wouldyouin._common.api.ApiResponse;
import org.ktc2.cokaen.wouldyouin._common.api.ApiResponseBody;
import org.ktc2.cokaen.wouldyouin.payment.application.PaymentService;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.KakaoPayReservationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoPaymentRedirectController {

    @Value("${oauth.payment.approval_deep_link}")
    private String approvalDeepLink;

    @Value("${oauth.payment.cancel_deep_link}")
    private String cancelDeepLink;

    @Value("${oauth.payment.fail_deep_link}")
    private String failDeepLink;

    private final PaymentService paymentService;

    @GetMapping("/why")
    public String payTest() {
        return "redirect:" + paymentService.readyPaymentTest();
    }

    @GetMapping("/kakaopay/redirect/approval")
    public String redirectKakaopayApproval(
        @RequestParam("pg_token") String pgToken, @RequestParam Long orderId) {
        log.debug("실행되는지 확인 테스트 pg: {}, id: {}", pgToken, orderId);
//        paymentService.approvePaymentTest(orderId, pgToken);
//        return "redirect:/payview";
//        return "redirect:" + "wouldyouin://booking/kakao/check/payment_approve" + "?reservationId=" + "1";
//            paymentService.approvePayment(orderId, pgToken);
        Long reservationId = paymentService.approvePayment(orderId, pgToken);
        return "redirect:" + "wouldyouin://booking/kakao/check/payment_approve" + "?reservationId=" + reservationId;
    }

    @GetMapping("payview")
    public String payView() {
        return "pay";
    }

    @GetMapping("/kakaopay/redirect/cancel")
    public String redirectKakaopayCancel(@RequestParam("pg_token") String pgToken) {
        String redirectDeeplink = "redirect:" + cancelDeepLink + "?pg_token=" + pgToken;
        log.debug("#### 캔슬 = {}", redirectDeeplink);
        return redirectDeeplink;
    }

    @GetMapping("/kakaopay/redirect/fail")
    public String redirectKakaopayFail(@RequestParam("pg_token") String pgToken) {
        String redirectDeeplink = "redirect:" + failDeepLink + "?pg_token=" + pgToken;
        log.debug("#### 페일 = {}", redirectDeeplink);
        return redirectDeeplink;
    }
}