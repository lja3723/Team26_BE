package org.ktc2.cokaen.wouldyouin.reservation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ktc2.cokaen.wouldyouin.auth.Authorize;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.ReservationRequest;
import org.ktc2.cokaen.wouldyouin.reservation.application.ReservationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakaoPaymentRedirectController {

    @Value("${oauth.payment.approval_url}")
    private String approvalUrl;

    @PostMapping("/kakaopay/redirect")
    public String createReservation1(@RequestParam("pg_token") String pgToken) {
        return "redirect:" + approvalUrl + "?pg_token=" + pgToken;
    }
}