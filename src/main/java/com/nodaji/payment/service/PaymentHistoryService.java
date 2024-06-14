package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.entity.PaymentHistory;
import com.nodaji.payment.global.domain.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;

    public void createPaymentHistory(JSONObject jsonObject, String userId) {
        paymentHistoryRepository.save(new PaymentHistory().toEntity(jsonObject,userId));
    }


}
