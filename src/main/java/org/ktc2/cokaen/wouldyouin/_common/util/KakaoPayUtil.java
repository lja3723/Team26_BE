package org.ktc2.cokaen.wouldyouin._common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.ktc2.cokaen.wouldyouin.payment.dto.KakaoPayRequest;
import org.ktc2.cokaen.wouldyouin.payment.persist.Payment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class KakaoPayUtil {

    public static HttpHeaders createKakaoPayRequestHeaders(String kakaoPayRequestHost, String secretKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "open-api.kakaopay.com");
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static Map<String, String> createKakaoPayRequestBody(Long orderId, KakaoPayRequest kakaoPayRequest) {
        Map<String, String> body = new HashMap<>();
        body.put("cid", kakaoPayRequest.getCid());
        body.put("partner_order_id", orderId + "");
        body.put("partner_user_id", kakaoPayRequest.getPartnerUserId());
        body.put("item_name", kakaoPayRequest.getItemName());
        body.put("quantity", kakaoPayRequest.getQuantity());
        body.put("total_amount", kakaoPayRequest.getTotalAmount());
        body.put("tax_free_amount", kakaoPayRequest.getTaxFreeAmount());
        body.put("approval_url", kakaoPayRequest.getApprovalUrl());
        body.put("cancel_url", kakaoPayRequest.getCancelUrl());
        body.put("fail_url", kakaoPayRequest.getFailUrl());
        return body;
    }

    public static Map<String, String> createPayCompleteRequestBody(Payment payment, String pgToken) {
        Map<String, String> body = new HashMap<>();
        body.put("cid", payment.getCid());
        body.put("tid", UUID.randomUUID().toString());
        body.put("partner_order_id", payment.getPartnerOrderId() + "");
        body.put("partner_user_id", payment.getPartnerUserId() + "");
        body.put("pg_token", pgToken);
        return body;
    }
}