package com.nodaji.payment.dto.request;


import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;

public record BuyRequestDto (String type, Long amount){

    public static History toEntity(String userId, BuyRequestDto req){
        return History.builder()
                .transaction_status(req.type())
                .price(req.amount())
                .createdAt(new Date())
                .userId(userId)
                .build();
    }
}
