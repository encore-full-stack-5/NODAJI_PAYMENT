package com.nodaji.payment.dto.response;


import org.json.simple.JSONObject;

public record PaymentErrorResponseDto(
        Integer code,
        String message
) {
    public static PaymentErrorResponseDto fromJSONObject(JSONObject jsonObject) {
        return new PaymentErrorResponseDto(
                Integer.parseInt(jsonObject.get("code").toString()),
                jsonObject.get("message").toString()
        );
    }
}