package org.ktc2.cokaen.wouldyouin.reservation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakaoPaymentRedirectController {

    @Value("${oauth.payment.approval_deep_link}")
    private String approvalDeepLink;

    @Value("${oauth.payment.cancel_deep_link}")
    private String cancelDeepLink;

    @Value("${oauth.payment.fail_deep_link}")
    private String failDeepLink;

    @PostMapping("/kakaopay/redirect/approval")
    public String redirectKakaopayApproval(@RequestParam("pg_token") String pgToken) {
        return "redirect:" + approvalDeepLink + "?pg_token=" + pgToken;
    }

    @PostMapping("/kakaopay/redirect/cancel")
    public String redirectKakaopayCancel(@RequestParam("pg_token") String pgToken) {
        return "redirect:" + cancelDeepLink + "?pg_token=" + pgToken;
    }

    @PostMapping("/kakaopay/redirect/fail")
    public String redirectKakaopayFail(@RequestParam("pg_token") String pgToken) {
        return "redirect:" + failDeepLink + "?pg_token=" + pgToken;
    }
}