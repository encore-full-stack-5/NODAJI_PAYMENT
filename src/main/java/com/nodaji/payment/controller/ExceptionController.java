package com.nodaji.payment.controller;

import com.nodaji.payment.global.domain.exception.AccountExistException;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.BalanceNotZeroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String accountNotFoundExceptionHandler(AccountNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(AccountExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String accountExistExceptionHandler(AccountExistException e){
        return e.getMessage();
    }

    @ExceptionHandler(BalanceNotZeroException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String BalanceNotZeroExceptionHandler(BalanceNotZeroException e){
        return e.getMessage();
    }

}