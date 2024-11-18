package org.ktc2.cokaen.wouldyouin.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = SnakeCaseStrategy.class)
public class PayCompleteResponse {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private CardInfo cardInfo;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String payload;

    @Getter
    @Setter
    @NoArgsConstructor
    static class Amount {
        private Integer total;
        private Integer taxFree;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer greenDeposit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
     static class CardInfo {
        private String kakaopayPurchaseCorp;
        private String kakaopayPurchaseCorpCode;
        private String kakaopayIssuerCorp;
        private String kakaopayIssuerCorpCode;
        private String bin;
        private String cardType;
        private String installMonth;
        private String approvedId;
        private String cardMid;
        private String interestFreeInstall;
        private String installmentType;
        private String cardItemCode;
    }
}
