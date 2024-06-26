package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.exception.*;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountServiceImpl;

    @Autowired
    HistoryService historyServiceImpl;

    @BeforeEach
    void setUp(){
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
    }

    @AfterEach
    void end(){
        accountRepository.deleteById("userId");
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    @Transactional
    void isExistAccount(){
        // when
        Boolean existAccount = accountServiceImpl.isExistAccount("userId");
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
        AccountExistException existException = assertThrows(AccountExistException.class,()-> accountServiceImpl.createAccount("userId"));
        // then
        assertEquals("해당 계좌가 이미 존재합니다.",existException.getMessage());
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    @Transactional
    void deleteAccount() {
        // given
        accountRepository.saveAndFlush(new Account("userId1",0L));

        accountServiceImpl.deleteAccount("userId1");
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
        BalanceNotZeroException NotZeroException = assertThrows(BalanceNotZeroException.class,()-> accountServiceImpl.deleteAccount("userId"));
        // then
        assertEquals("예치금 잔액이 남아있습니다.",NotZeroException.getMessage());
    }

    @Test
    @DisplayName("포인트 조회 테스트")
    @Transactional
    void getPoint() {
        // when
        PointResponseDto userId = accountServiceImpl.getPoint("userId");
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
        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,()-> accountServiceImpl.getPoint("userId"));
        // then
        assertEquals("해당 계좌가 없습니다.",accountNotFoundException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("결제시 예치금 차감 테스트")
    void deductPoint(){
        // when
        accountServiceImpl.deductPoint("userId",new BuyRequestDto("결제", 1000L));
        PointResponseDto userId = accountServiceImpl.getPoint("userId");
        // then
        assertEquals(9000L,userId.point());
    }

    @Test
    @DisplayName("결제시 결제금액이 예치금보다 클 때 예외 테스트")
    void deductPointMoreThanDeposit(){
        // when
        BalanceNotEnoughException balanceNotEnoughException = assertThrows(BalanceNotEnoughException.class,()-> accountServiceImpl.deductPoint("userId",new BuyRequestDto("결제", 100000L)));
        // then
        assertEquals("예치금 잔액이 부족합니다.",balanceNotEnoughException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("예치금 충전 테스트")
    void depositPoint() {
        // when
        accountServiceImpl.depositPoint("userId",10000L);
        PointResponseDto userId = accountServiceImpl.getPoint("userId");
        // then
        assertEquals(20000L,userId.point());
    }

    @Test
    @DisplayName("예치금 출금 테스트")
    void withdrawPoint() throws InterruptedException {
        // given
        CountDownLatch latch = new CountDownLatch(1);
        accountServiceImpl.withdrawPoint("userId",new WithdrawRequestDto(5000L,5000L,"신한","123456789"));
        // wait for the async operation to complete
        latch.await(1, TimeUnit.SECONDS);
        // when
        PointResponseDto userId = accountServiceImpl.getPoint("userId");
        // then
        assertEquals(0L,userId.point());
    }

    @Test
    @DisplayName("예치금 출금 예외 테스트(출금 금액이 예치금 + 수수료 보다 많을때")
    void withdrawPointMoreThanDeposit() {
        // when
        ExceedsBalanceException exceedsBalanceException = assertThrows(ExceedsBalanceException.class,()-> accountServiceImpl.withdrawPoint("userId",new WithdrawRequestDto(5000L,5001L,"신한","123456789")));
        // then
        assertEquals("출금하려는 금액이 예치금과 수수료의 합보다 많습니다.",exceedsBalanceException.getMessage());
    }



}