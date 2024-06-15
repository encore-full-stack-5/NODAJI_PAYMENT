package com.nodaji.payment.global.domain.exception;

public class ExceedsBalanceException extends IllegalArgumentException{
    public ExceedsBalanceException() {
        super("출금하려는 금액이 예치금과 수수료의 합보다 많습니다.");
    }
}
