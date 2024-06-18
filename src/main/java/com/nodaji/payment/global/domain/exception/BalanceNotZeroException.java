package com.nodaji.payment.global.domain.exception;

public class BalanceNotZeroException extends IllegalArgumentException{
    public BalanceNotZeroException() {
        super("예치금 잔액이 남아있습니다.");
    }
}
