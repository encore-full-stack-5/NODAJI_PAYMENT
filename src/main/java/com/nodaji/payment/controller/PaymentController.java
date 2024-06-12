package com.nodaji.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    /**
     * 결제 요청
     */
    @PostMapping("/{userId}")
    public void pay(@PathVariable("userId") String userId){

    };
}
