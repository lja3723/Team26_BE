package org.ktc2.cokaen.wouldyouin.reservation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/kakaopay/redirect/approval")
    public String redirectKakaopayApproval(@RequestParam("pg_token") String pgToken) {
        String redirectDeeplink = "redirect:" + approvalDeepLink + "?pg_token=" + pgToken;
        log.debug("#### redirectDeeplink = {}", redirectDeeplink);
        return redirectDeeplink;
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