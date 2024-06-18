package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import com.nodaji.payment.dto.response.PaymentSuccessResponseDto;
import com.nodaji.payment.global.domain.entity.PaymentHistory;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.repository.PaymentHistoryRepository;
import com.nodaji.payment.utils.PaymentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final AccountService accountService;
    private final PaymentUtils paymentUtils;


    @Transactional
    public Object processPayment(String userId, String orderId, Long amount, String paymentKey) throws Exception {
//        게좌가 존재하는지 검증
        if(!accountService.isExistAccount(userId)) throw new AccountNotFoundException();

        String authorizations = paymentUtils.getAuthorizations();
        HttpURLConnection connection = paymentUtils.getHttpURLConnection(paymentKey, authorizations);
        paymentUtils.sendRequest(orderId, amount, connection);
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;
        JSONObject jsonObject = paymentUtils.getResponseJsonObject(connection, isSuccess);
        if (isSuccess) {
//            예치금 충전
            accountService.depositPoint(userId,amount);
//            충전 내역 저장
            accountService.createDepositHistory(userId,amount);
//            결제 내역 저장
            createPaymentHistory(jsonObject, userId);

            return PaymentSuccessResponseDto.fromJSONObject(jsonObject);
        } else {
            return PaymentErrorResponseDto.fromJSONObject(jsonObject);
        }
    }

    @Override
    public void createPaymentHistory(JSONObject jsonObject, String userId) {
        paymentHistoryRepository.save(new PaymentHistory().toEntity(jsonObject,userId));
    }
}