package com.nodaji.payment.global.domain.dto;

import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;

public record WinDepositDto (String userId, String type,String transactionStatus, Long amount){
    public History toEntity(WinDepositDto req){
        return History.builder()
                .transactionStatus(req.type+" 당첨금 수령")
                .price(req.amount)
                .createdAt(new Date())
                .userId(req.userId)
                .withdrawBankName(null)
                .withdrawAccountNum(null)
                .build();
    }
}