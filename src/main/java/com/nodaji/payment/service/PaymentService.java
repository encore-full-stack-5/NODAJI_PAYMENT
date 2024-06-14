package com.nodaji.payment.service;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> processPayment(String userId, String orderId, Long amount, String paymentKey) throws Exception;
}
