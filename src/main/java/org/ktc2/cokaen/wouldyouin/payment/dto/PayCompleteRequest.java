package org.ktc2.cokaen.wouldyouin.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin.payment.persist.Payment;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = SnakeCaseStrategy.class)
@ToString
public class PayCompleteRequest {
    private String cid;
    private String tid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;

    public static PayCompleteRequest from(Payment payment, String pgToken) {
        return PayCompleteRequest.builder()
            .cid(payment.getCid())
            .tid(payment.getTid())
            .partnerOrderId(payment.getPartnerOrderId() + "")
            .partnerUserId(payment.getPartnerUserId() + "")
            .pgToken(pgToken)
            .build();
    }
}
