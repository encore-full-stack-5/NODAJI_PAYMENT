package com.nodaji.payment.global.domain.exception;

public class UserNotFoundException extends IllegalArgumentException{
    public UserNotFoundException() {
        super("해당 유저를 찾을 수 없습니다.");
    }
}
