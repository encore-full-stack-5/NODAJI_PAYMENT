package com.nodaji.payment.global.domain.exception;

public class UserNotFoundException extends IllegalArgumentException{
    public UserNotFoundException() {
        super("User Not Found Exception");
    }
}
