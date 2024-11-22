package org.ktc2.cokaen.wouldyouin.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.ktc2.cokaen.wouldyouin._common.exception.BusinessException;
import org.ktc2.cokaen.wouldyouin._common.exception.ErrorCode;
import org.ktc2.cokaen.wouldyouin.member.persist.AccountType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@RestController
@Slf4j
@Controller
public class AuthRedirectController {

    @Value("${oauth.kakao.redirect.deeplink}")
    private String kakaoRedirectDeeplink;

    @Value("${oauth.google.redirect.deeplink}")
    private String googleRedirectDeeplink;

    private String getRedirectDeeplink(AccountType accountType) {
        switch(accountType) {
            case kakao:
                return kakaoRedirectDeeplink;
            case google:
                return googleRedirectDeeplink;
            default:
                throw new BusinessException("Invalid account type: " + accountType, ErrorCode.UNEXPECTED);
        }
    }

    @GetMapping("/auth/redirect/social/{accountType}")
    public String/*ResponseEntity<ApiResponseBody<String>>*/ redirect(@PathVariable("accountType")AccountType accountType, @RequestParam("code") String code) {
        String redirectDeeplink = "redirect:" + getRedirectDeeplink(accountType) + "?code=" + code;
        log.debug("#### redirectDeeplink = {}", redirectDeeplink);
        return redirectDeeplink;
//        return ApiResponse.ok("redirect:" + getRedirectUri(accountType) + "?code=" + code);
    }
}
