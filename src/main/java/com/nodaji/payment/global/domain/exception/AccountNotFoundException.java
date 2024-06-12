package com.nodaji.payment.global.domain.exception;

public class AccountNotFoundException extends IllegalArgumentException{
    public AccountNotFoundException() {
        super("Account Not Found");
    }
}
