package com.nodaji.payment.service;

import org.json.simple.JSONObject;

public interface PaymentHistoryService {
    void createPaymentHistory(JSONObject jsonObject, String userId);
}
