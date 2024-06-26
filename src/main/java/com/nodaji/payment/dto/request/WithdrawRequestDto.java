package com.nodaji.payment.dto.request;

import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;

public record WithdrawRequestDto (
        Long price,
        Long charge,
        String bankName,
        String accountNum
){
    public History toEntity(String userId, WithdrawRequestDto req){
        return History.builder()
                .transactionStatus("출금")
                .price(req.price()+req.charge())
                .createdAt(new Date())
                .userId(userId)
                .withdrawBankName(req.bankName())
                .withdrawAccountNum(req.accountNum())
                .build();
    }
}
