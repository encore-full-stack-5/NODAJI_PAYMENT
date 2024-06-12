package com.nodaji.payment.global.domain.exception;

public class AccountExistException extends IllegalArgumentException{
    public AccountExistException() {
        super("Account Already Exist");
    }
}
