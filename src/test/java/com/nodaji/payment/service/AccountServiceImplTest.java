package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.exception.*;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    HistoryServiceImpl historyService;

    @BeforeEach
    void setUp(){
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    @Transactional
    void isExistAccount(){
        // when
        Boolean existAccount = accountService.isExistAccount("userId");
        // then
        assertEquals(true,existAccount);
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    @Transactional
    void createAccount() {
        // when
        Account byUserId = accountRepository.findByUserId("userId");
        // then
        assertEquals("userId", byUserId.getUserId());
        assertEquals(10000L, byUserId.getPoint());
    }

    @Test
    @DisplayName("계좌 중복 생성 예외 테스트")
    @Transactional
    void createExistAccount() {
        // when
        AccountExistException existException = assertThrows(AccountExistException.class,()->accountService.createAccount("userId"));
        // then
        assertEquals("해당 계좌가 이미 존재합니다.",existException.getMessage());
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    @Transactional
    void deleteAccount() {
        // given
        accountService.createAccount("userId1");
        accountService.deleteAccount("userId1");
        // when
        boolean existsById = accountRepository.existsById("userId1");
        // then
        assertFalse(existsById);
    }

    @Test
    @DisplayName("계좌 삭제시 예치금이 0원보다 클 때 예외 테스트")
    @Transactional
    void deleteAccountMoreThanZeroException() {
        // when
        BalanceNotZeroException NotZeroException = assertThrows(BalanceNotZeroException.class,()->accountService.deleteAccount("userId"));
        // then
        assertEquals("예치금 잔액이 남아있습니다.",NotZeroException.getMessage());
    }

    @Test
    @DisplayName("포인트 조회 테스트")
    @Transactional
    void getPoint() {
        // when
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(10000L,userId.point());
    }

    @Test
    @DisplayName("포인트 조회 예외 테스트(해당 계좌가 존재하지 않을 때)")
    @Transactional
    void getPointNotExistAccountException() {

        // given
        accountRepository.deleteById("userId");
        // when
        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,()->accountService.getPoint("userId"));
        // then
        assertEquals("해당 계좌가 없습니다.",accountNotFoundException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("결제시 예치금 차감 테스트")
    void deductPoint(){
        // when
        accountService.deductPoint("userId",new BuyRequestDto("결제", 1000L));
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(9000L,userId.point());
    }

    @Test
    @Transactional
    @DisplayName("결제시 결제금액이 예치금보다 클 때 예외 테스트")
    void deductPointMoreThanDeposit(){
        // when
        BalanceNotEnoughException balanceNotEnoughException = assertThrows(BalanceNotEnoughException.class,()->accountService.deductPoint("userId",new BuyRequestDto("결제", 100000L)));
        // then
        assertEquals("예치금 잔액이 부족합니다.",balanceNotEnoughException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("예치금 충전 테스트")
    void depositPoint() {
        // when
        accountService.depositPoint("userId",10000L);
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(20000L,userId.point());
    }

    @Test
    @DisplayName("예치금 출금 테스트")
    void withdrawPoint() {
        // given
        accountService.withdrawPoint("userId",new WithdrawRequestDto(5000L,5000L,"신한","123456789"));
        // when
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(0L,userId.point());
    }

    @Test
    @DisplayName("예치금 출금 예외 테스트(출금 금액이 예치금 + 수수료 보다 많을때")
    void withdrawPointMoreThanDeposit() {
        // when
        ExceedsBalanceException exceedsBalanceException = assertThrows(ExceedsBalanceException.class,()->accountService.withdrawPoint("userId",new WithdrawRequestDto(5000L,5001L,"신한","123456789")));
        // then
        assertEquals("출금하려는 금액이 예치금과 수수료의 합보다 많습니다.",exceedsBalanceException.getMessage());
    }


    @Test
    @Transactional
    @DisplayName("예치금 거래내역 조회 테스트")
    void getTransactionHistory() throws ParseException {
        // given
        historyService.createDepositHistory("userId1",20000L);
        historyService.createWithdrawHistory("userId1",new WithdrawRequestDto(500L,500L,"신한","123456789"));
        historyService.createBuyHistory("userId1",new BuyRequestDto("동행복권결제",10000L));
        // when
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2023-01-01");
        Date endDate = dateFormat.parse("2100-12-31");
        List<History> userId1 = historyService.getTransactionHistory("userId1",startDate,endDate);
        // then
        assertEquals(userId1.get(0).getPrice(),10000L);
        assertEquals(userId1.get(1).getPrice(),1000L);
        assertEquals(userId1.get(2).getPrice(),20000L);
    }
}