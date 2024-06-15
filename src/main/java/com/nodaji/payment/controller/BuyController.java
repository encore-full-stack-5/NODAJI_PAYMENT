package com.nodaji.payment.controller;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;
import com.nodaji.payment.service.BuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BuyController {
    private final BuyService buyService;

    /**
     * 구매 요청
     */
    @PostMapping("/buy/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public BuyResponseDto buy(@PathVariable("userId") String userId, @RequestBody BuyRequestDto req){
        return buyService.buyItems(userId, req);
    };
}


