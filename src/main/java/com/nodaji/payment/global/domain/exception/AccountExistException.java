package com.nodaji.payment.global.domain.exception;

public class AccountExistException extends IllegalArgumentException{
    public AccountExistException() {
        super("해당 계좌가 이미 존재합니다.");
    }
}
