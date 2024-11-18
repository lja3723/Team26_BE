package org.ktc2.cokaen.wouldyouin.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.event.persist.Event;
import org.ktc2.cokaen.wouldyouin.payment.persist.Payment;
import org.ktc2.cokaen.wouldyouin.reservation.api.dto.ReservationRequest;

@Getter
@Builder
@ToString
@AllArgsConstructor
@JsonNaming(value = SnakeCaseStrategy.class)
public class KakaoPayRequest {

    private String cid;
    private String partnerUserId;
    private String itemName;
    private String quantity;
    private String totalAmount;
    private String taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;

    public static KakaoPayRequest of(
        MemberIdentifier identifier, Event event, ReservationRequest request,
        String approvalUrl, String cancelUrl, String failUrl) {
        return KakaoPayRequest.builder()
            .cid("WOULDYOUIN")
            .partnerUserId(identifier.id() + "")
            .itemName(event.getTitle())
            .quantity(request.getQuantity() + "")
            .totalAmount(event.getPrice() * request.getQuantity() + "")
            .taxFreeAmount("0")
            .approvalUrl(approvalUrl)
            .cancelUrl(cancelUrl)
            .failUrl(failUrl)
            .build();
    }

    public Payment toEntity(ReservationRequest request) {
        return Payment.builder()
            .cid(cid)
            .partnerUserId(Long.parseLong(partnerUserId))
            .itemName(itemName)
            .quantity(request.getQuantity())
            .eventId(request.getEventId())
            .build();
    }
}