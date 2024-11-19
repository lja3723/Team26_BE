package org.ktc2.cokaen.wouldyouin.reservation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktc2.cokaen.wouldyouin.auth.Authorize;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.ktc2.cokaen.wouldyouin.payment.application.PaymentService;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.ReservationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    @GetMapping("/kakaopay")
    public String redirectKakaopay(RedirectAttributes redirectAttribute) {
        return "redirect:" + paymentService.readyPaymentTest();
    }

//    @PostMapping("/api/reservations")
//    public String createReservation(
//        @Valid @RequestBody ReservationRequest reservationRequest,
//        @Authorize({MemberType.normal, MemberType.curator}) MemberIdentifier identifier
//    ) {
//        return paymentService.readyPayment(identifier, reservationRequest);
//    }

    @GetMapping("/kakaopay/redirect/approval")
    public String redirectKakaopayApproval(
        @RequestParam("pg_token") String pgToken, @RequestParam Long orderId) {
        return "redirect:" + approvalDeepLink + "?reservationId=" + paymentService.approvePayment(orderId, pgToken);
    }

    @GetMapping("payview")
    public String payView() {
        return "pay";
    }

    @GetMapping("/kakaopay/redirect/cancel")
    public String redirectKakaopayCancel(@RequestParam("pg_token") String pgToken) {
        String redirectDeeplink = "redirect:" + cancelDeepLink + "?pg_token=" + pgToken;
        log.debug("#### redirectDeeplink = {}", redirectDeeplink);
        return redirectDeeplink;
    }

    @GetMapping("/kakaopay/redirect/fail")
    public String redirectKakaopayFail(@RequestParam("pg_token") String pgToken) {
        String redirectDeeplink = "redirect:" + failDeepLink + "?pg_token=" + pgToken;
        log.debug("#### redirectDeeplink = {}", redirectDeeplink);
        return redirectDeeplink;
    }
}