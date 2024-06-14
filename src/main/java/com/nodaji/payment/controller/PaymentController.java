package com.nodaji.payment.controller;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import com.nodaji.payment.dto.response.PaymentSuccessResponseDto;
import com.nodaji.payment.service.PaymentHistoryService;
import com.nodaji.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentHistoryService paymentInfoService;

    @GetMapping("/success/{userId}")
    public Object paymentCont(
            @PathVariable(name = "userId") String userId,
            @RequestParam(name = "orderId") String orderId,
            @RequestParam(name = "amount") Integer amount,
            @RequestParam(name = "paymentKey") String paymentKey) throws Exception {

        Map<String, Object> result = paymentService.processPayment(userId, orderId, amount, paymentKey);
        boolean isSuccess = (boolean) result.get("isSuccess");
        JSONObject jsonObject = (JSONObject) result.get("response");

        if (isSuccess) {
            paymentInfoService.createPaymentHistory(jsonObject, userId);
            return PaymentSuccessResponseDto.fromJSONObject(jsonObject);
        } else {
            return PaymentErrorResponseDto.fromJSONObject(jsonObject);
        }
    }

    @GetMapping("/fail")
    public PaymentErrorResponseDto paymentResult(
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) {

        return new PaymentErrorResponseDto(code, message);
    }
}