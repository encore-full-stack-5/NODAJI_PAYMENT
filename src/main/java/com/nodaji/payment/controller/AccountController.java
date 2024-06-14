package com.nodaji.payment.controller;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.dto.AccountDto;
import com.nodaji.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AccountController {
    private final AccountService accountService;

    /**
     * 예치금 계좌 생성
     */
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@PathVariable("userId") String userId){
        accountService.createAccount(userId);
    }

    /**
     * 예치금 계좌 삭제
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable("userId") String userId){
        accountService.deleteAccount(userId);
    }

    /**
     * 예치금 확인
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PointResponseDto getPoint(@PathVariable("userId") String userId){
        return accountService.getPoint(userId);
    }

    /**
     * 예치금 충전 (타 결제 시스템과 연동)
     */
    @PostMapping("/{userId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void depositToAccount(@PathVariable("userId")  String userId, @RequestBody DepositRequestDto req){

        accountService.depositPoint(userId, req);
    }
//
    /**
     * 예치금 출금 (다른 은행 시스템과 연동)
     */
    @PostMapping("/{userId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawFromAccount(@PathVariable("userId") String userId, @RequestBody DepositRequestDto req){
        accountService.withdrawPoint(userId, req);
    }

    /**
     * 예치금 거래 내역 조회
     */
    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public void getTransactionHistory(@PathVariable("userId") String userId){
        accountService.getTransactionHistory(userId);
    }

    /**
     * 결제 요청
     */
    @PostMapping("/account/{userId}/pay")
    @ResponseStatus(HttpStatus.OK)
    public void pay(@PathVariable("userId") String userId){

    };
}