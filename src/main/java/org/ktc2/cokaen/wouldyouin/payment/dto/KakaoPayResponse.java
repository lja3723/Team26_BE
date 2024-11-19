package org.ktc2.cokaen.wouldyouin.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@JsonNaming(value = SnakeCaseStrategy.class)
public class KakaoPayResponse {

    private  String tid;
    private  String nextRedirectAppUrl;
    private  String nextRedirectMobileUrl;
    private  String nextRedirectPcUrl;
    private  String androidAppScheme;
    private  String iosAppScheme;
    private  LocalDateTime createdAt;
}