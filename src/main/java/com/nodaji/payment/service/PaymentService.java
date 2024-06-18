package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import org.json.simple.JSONObject;

public interface PaymentService {
    Object processPayment(String userId, String orderId, Long amount, String paymentKey) throws Exception;
    void createPaymentHistory(JSONObject jsonObject, String userId);
}
