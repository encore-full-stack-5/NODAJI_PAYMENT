package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;

public interface PaymentService {
    Object processPayment(String userId, String orderId, Long amount, String paymentKey) throws Exception;
}
