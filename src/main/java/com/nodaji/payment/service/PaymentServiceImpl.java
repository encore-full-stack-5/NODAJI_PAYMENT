package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import com.nodaji.payment.dto.response.PaymentSuccessResponseDto;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.entity.PaymentHistory;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final AccountService accountService;

    @Value("${paymentSecretKey}")
    private String paymentSecretKey;
    @Transactional
    public Object processPayment(String userId, String orderId, Long amount, String paymentKey) throws Exception {
//        게좌가 존재하는지 검증
        if(!accountService.isExistAccount(userId)) throw new AccountNotFoundException();

        String authorizations = getAuthorizations();
        HttpURLConnection connection = getHttpURLConnection(paymentKey, authorizations);
        sendRequest(orderId, amount, connection);
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;
        JSONObject jsonObject = getResponseJsonObject(connection, isSuccess);

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


    private static JSONObject getResponseJsonObject(HttpURLConnection connection, boolean isSuccess) throws IOException, ParseException {
        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        return jsonObject;
    }

    private static void sendRequest(String orderId, Long amount, HttpURLConnection connection) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));
    }

    private static HttpURLConnection getHttpURLConnection(String paymentKey, String authorizations) throws IOException {
        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    private String getAuthorizations() throws UnsupportedEncodingException {
        String secretKey = paymentSecretKey + ":";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);
        return authorizations;
    }

}