package com.nodaji.payment.global.domain.dto;

import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;

public record WinDepositDto (String type,String transactionStatus, Long amount){
    public History toEntity(String userId, WinDepositDto req){
        return History.builder()
                .transactionStatus(req.type+" 당첨금 수령")
                .price(req.amount)
                .createdAt(new Date())
                .userId(userId)
                .withdrawBankName(null)
                .withdrawAccountNum(null)
                .build();
    }
}