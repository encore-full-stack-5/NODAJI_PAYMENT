package com.nodaji.payment.controller;

import com.nodaji.payment.global.domain.dto.AccountDto;
import com.nodaji.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
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
    public void createAccount(@PathVariable("userId") String userId){
        accountService.createAccount(userId);
    }

    /**
     * 예치금 계좌 삭제
     */
    @DeleteMapping("/{userId}")
    public void deleteAccount(@PathVariable("userId") String userId){
        accountService.deleteAccount(userId);
    }

    /**
     * 예치금 확인
     */
    @GetMapping("/{userId}")
    public Long getPoint(@PathVariable("userId") String userId){
        return accountService.getPoint(userId);
    }

    /**
     * 예치금 충전 (타 결제 시스템과 연동)
     */
    @PostMapping("/{userId}/deposit")
    public void depositToAccount(@PathVariable("userId") String userId){

    }

    /**
     * 예치금 출금 (다른 은행 시스템과 연동)
     */
    @PostMapping("/{userId}/withdraw")
    public void withdrawFromAccount(@PathVariable("userId") String userId){

    }

    /**
     * 예치금 거래 내역 조회
     */
    @GetMapping("/{userId}/history")
    public void getTransactionHistory(@PathVariable("userId") String userId){

    }

    /**
     * 결제 요청
     */
    @PostMapping("/account/{userId}/pay")
    public void pay(@PathVariable("userId") String userId){

    };
}