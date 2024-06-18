package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.exception.*;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountServiceImpl accountService;


    @Test
    @DisplayName("계좌 생성 테스트")
    @Transactional
    void isExistAccount(){
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        Boolean existAccount = accountService.isExistAccount("userId");
        // then
        assertEquals(true,existAccount);
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    @Transactional
    void createAccount() {
        // given
        accountService.createAccount("userId");
        // when
        Account byUserId = accountRepository.findByUserId("userId");
        // then
        assertEquals("userId", byUserId.getUserId());
        assertEquals(0L, byUserId.getPoint());
    }

    @Test
    @DisplayName("계좌 중복 생성 예외 테스트")
    @Transactional
    void createExistAccount() {
        // given
        accountService.createAccount("userId");
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
        Account account = new Account("userId",0L);
        accountRepository.save(account);
        accountService.deleteAccount("userId");
        // when
        boolean existsById = accountRepository.existsById("userId");
        // then
        assertFalse(existsById);
    }

    @Test
    @DisplayName("계좌 삭제시 예치금이 0원보다 클 때 예외 테스트")
    @Transactional
    void deleteAccountMoreThanZeroException() {
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        BalanceNotZeroException NotZeroException = assertThrows(BalanceNotZeroException.class,()->accountService.deleteAccount("userId"));
        // then
        assertEquals("예치금 잔액이 남아있습니다.",NotZeroException.getMessage());
    }

    @Test
    @DisplayName("포인트 조회 테스트")
    @Transactional
    void getPoint() {
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(10000L,userId.point());
    }

    @Test
    @DisplayName("포인트 조회 예외 테스트(해당 계좌가 존재하지 않을 때)")
    @Transactional
    void getPointNotExistAccountException() {
        // given & when
        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,()->accountService.getPoint("userId"));
        // then
        assertEquals("해당 계좌가 없습니다.",accountNotFoundException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("결제시 예치금 차감 테스트")
    void deductPoint(){
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
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
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        BalanceNotEnoughException balanceNotEnoughException = assertThrows(BalanceNotEnoughException.class,()->accountService.deductPoint("userId",new BuyRequestDto("결제", 100000L)));
        // then
        assertEquals("예치금 잔액이 부족합니다.",balanceNotEnoughException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("예치금 충전 테스트")
    void depositPoint() {
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        accountService.depositPoint("userId",10000L);
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(20000L,userId.point());
    }

    @Test
    @Transactional
    @DisplayName("예치금 출금 테스트")
    void withdrawPoint() {
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        accountService.withdrawPoint("userId",new WithdrawRequestDto(5000L,5000L,"신한","123-456-789","ownerName"));
        // when
        PointResponseDto userId = accountService.getPoint("userId");
        // then
        assertEquals(0L,userId.point());
    }

    @Test
    @Transactional
    @DisplayName("예치금 출금 예외 테스트(출금 금액이 예치금 + 수수료 보다 많을때")
    void withdrawPointMoreThanDeposit() {
        // given
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        // when
        ExceedsBalanceException exceedsBalanceException = assertThrows(ExceedsBalanceException.class,()->accountService.withdrawPoint("userId",new WithdrawRequestDto(5000L,5001L,"신한","123-456-789","ownerName")));
        // then
        assertEquals("출금하려는 금액이 예치금과 수수료의 합보다 많습니다.",exceedsBalanceException.getMessage());
    }


    @Test
    @Transactional
    @DisplayName("예치금 거래내역 조회 테스트")
    void getTransactionHistory() {
        // given
        accountService.createDepositHistory("userId1",20000L);
        accountService.createWithdrawHistory("userId1",new WithdrawRequestDto(500L,500L,"신한","123-456-789","ownerName"));
        accountService.createBuyHistory("userId1",new BuyRequestDto("동행복권결제",10000L));
        // when
        List<History> userId1 = accountService.getTransactionHistory("userId1");
        // then
        assertEquals(userId1.get(0).getPrice(),10000L);
        assertEquals(userId1.get(1).getPrice(),1000L);
        assertEquals(userId1.get(2).getPrice(),20000L);
    }
}