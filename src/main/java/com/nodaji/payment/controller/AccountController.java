package com.nodaji.payment.controller;

import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.service.AccountService;
import com.nodaji.payment.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final HistoryService historyService;

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
     * 당첨금 입금 (Feign)
     */
    @PutMapping("/{userId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void depositToAccount(@PathVariable("userId")  String userId, @RequestBody WinDepositDto req){
        accountService.depositWinPoint(userId,req);
    }

    /**
     * 예치금 출금 (다른 은행 시스템과 연동)
     */
    @PutMapping("/{userId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawFromAccount(@PathVariable("userId") String userId, @RequestBody WithdrawRequestDto req){
        accountService.withdrawPoint(userId, req);
    }

    /**
     * 예치금 거래 내역 조회
     */
    @GetMapping("/{userId}/histories")
    @ResponseStatus(HttpStatus.OK)
    public List<History> getTransactionHistory(
            @PathVariable("userId") String userId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        return historyService.getTransactionHistory(userId,startDate, endDate);
    }
    /**
     * 출금 거래 내역 조회
     */
    @GetMapping("/{userId}/histories/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public List<History> getWithdrawHistory(
            @PathVariable("userId") String userId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        return historyService.getWithdrawHistory(userId,startDate, endDate);
    }
}