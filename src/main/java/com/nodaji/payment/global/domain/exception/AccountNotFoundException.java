package com.nodaji.payment.global.domain.exception;

public class AccountNotFoundException extends IllegalArgumentException{
    public AccountNotFoundException() {
        super("해당 계좌가 없습니다.");
    }
}
