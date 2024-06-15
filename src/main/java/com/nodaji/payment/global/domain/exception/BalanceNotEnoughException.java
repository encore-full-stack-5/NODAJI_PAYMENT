package com.nodaji.payment.global.domain.exception;

public class BalanceNotEnoughException extends IllegalArgumentException{
    public BalanceNotEnoughException() {
        super("예치금 잔액이 부족합니다.");
    }
}