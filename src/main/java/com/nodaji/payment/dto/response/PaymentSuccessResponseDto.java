package com.nodaji.payment.dto.response;


import org.json.simple.JSONObject;

public record PaymentSuccessResponseDto(
        boolean isSuccess,
        String orderId,
        String type,
        String method,
        String totalAmount,
        String status,
        String approvedAt,
        String orderName

) {
    public static PaymentSuccessResponseDto fromJSONObject(JSONObject jsonObject) {
        return new PaymentSuccessResponseDto(
                true,
                jsonObject.get("orderId").toString(),
                jsonObject.get("type").toString(),
                jsonObject.get("method").toString(),
                jsonObject.get("totalAmount").toString(),
                jsonObject.get("status").toString(),
                jsonObject.get("approvedAt").toString(),
                jsonObject.get("orderName").toString()
        );
    }
}