package org.ktc2.cokaen.wouldyouin.reservation.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ktc2.cokaen.wouldyouin.auth.Authorize;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.ReservationRequest;
import org.ktc2.cokaen.wouldyouin.reservation.application.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ReservationRedirectController {

    private final ReservationService reservationService;

    @PostMapping("/redirect1")
    public String createReservation1(
        @Valid @RequestBody ReservationRequest reservationRequest,
        @Authorize({MemberType.normal, MemberType.curator}) MemberIdentifier identifier
    ) {
        return "redirect:" + reservationService.create(identifier, reservationRequest).getKakaoPayResponse().getNextRedirectAppUrl();
    }

    @PostMapping("/redirect2")
    public String createReservation2(
        @Valid @RequestBody ReservationRequest reservationRequest,
        @Authorize({MemberType.normal, MemberType.curator}) MemberIdentifier identifier
    ) {
        return "redirect:" + reservationService.create(identifier, reservationRequest).getKakaoPayResponse().getAndroidAppScheme();
    }
}