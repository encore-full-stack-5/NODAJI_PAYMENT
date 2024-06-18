package com.nodaji.payment.dto.request;


import com.nodaji.payment.global.domain.entity.WinResult;

public record WinRequestDto (String type, Integer leftMonths, Long amount){
    public WinResult toEntity(String userId,WinRequestDto req){
        return WinResult.builder()
                .resultId(null)
                .leftMonths(req.leftMonths())
                .amount(req.amount())
                .userId(userId)
                .build();
    }
}