package com.nodaji.payment.controller;

import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.entity.UserDto;
import com.nodaji.payment.service.AccountService;
import com.nodaji.payment.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final HistoryService historyService;

    /**
     * 계좌 생성
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void creatAccount(){
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.createAccount(principal.getId());
    }
    /**
     * 예치금 계좌 삭제
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(){
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.deleteAccount(principal.getId());
    }

    /**
     * 예치금 확인
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PointResponseDto getPoint(@PathVariable(name = "userId") String userId){
        return accountService.getPoint(userId);
    }

    /**
     * 당첨금 입금 (Feign)
     */
    @PutMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void depositToAccount(@RequestBody WinDepositDto req){
        accountService.depositWinPoint(req);
    }

    /**
     * 예치금 출금 (다른 은행 시스템과 연동)
     */
    @PutMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawFromAccount(@RequestBody WithdrawRequestDto req){
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.getUsername());
        accountService.withdrawPoint(principal.getId(), req);
    }

    /**
     * 예치금 거래 내역 조회
     */
    @GetMapping("/histories")
    @ResponseStatus(HttpStatus.OK)
    public List<History> getTransactionHistory(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return historyService.getTransactionHistory(principal.getId(),startDate, endDate);
    }
    /**
     * 출금 거래 내역 조회
     */
    @GetMapping("/histories/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public List<History> getWithdrawHistory(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return historyService.getWithdrawHistory(principal.getId(),startDate, endDate);
    }
}