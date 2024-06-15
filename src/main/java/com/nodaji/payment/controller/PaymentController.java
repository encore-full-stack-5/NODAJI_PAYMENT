package com.nodaji.payment.controller;

import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import com.nodaji.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/success/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Object submitPayment(
            @PathVariable(name = "userId") String userId,
            @RequestParam(name = "orderId") String orderId,
            @RequestParam(name = "amount") Long amount,
            @RequestParam(name = "paymentKey") String paymentKey) throws Exception {
        return paymentService.processPayment(userId, orderId, amount, paymentKey);
    }

    @GetMapping("/fail")
    @ResponseStatus(HttpStatus.CONFLICT)
    public PaymentErrorResponseDto paymentResult(
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) {

        return new PaymentErrorResponseDto(code, message);
    }
}