package com.nodaji.payment.controller;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;
import com.nodaji.payment.dto.response.PaymentErrorResponseDto;
import com.nodaji.payment.global.domain.entity.UserDto;
import com.nodaji.payment.service.AccountService;
import com.nodaji.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AccountService accountService;

    /**
     * 구매 요청
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public BuyResponseDto buy(@PathVariable(name = "userId") String userId, @RequestBody BuyRequestDto req){
        return accountService.buyItems(userId, req);
    };
    @PostMapping("/success")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject submitPayment(
            @RequestParam(name = "orderId") String orderId,
            @RequestParam(name = "amount") Long amount,
            @RequestParam(name = "paymentKey") String paymentKey) throws Exception {
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return paymentService.processPayment(principal.getId(), orderId, amount, paymentKey);
    }
    @PostMapping("/fail")
    @ResponseStatus(HttpStatus.CONFLICT)
    public PaymentErrorResponseDto paymentResult(
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) {

        return new PaymentErrorResponseDto(code, message);
    }
}