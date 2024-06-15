package com.nodaji.payment.dto.request;

public record WithdrawRequestDto (
        Long price,
        Long charge,
        String bankName,
        String accountNum,
        String accountOwnerName
){
}
