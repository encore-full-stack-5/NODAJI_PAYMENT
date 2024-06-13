package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.AccountExistException;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.BalanceNotZeroException;
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
    void createAccount() {
        accountService.createAccount("userId");
        Account byUserId = accountRepository.findByUserId("userId");
        assertEquals("userId", byUserId.getUserId());
        assertEquals(0L, byUserId.getPoint());
    }

    @Test
    @DisplayName("계좌 중복 생성 예외 테스트")
    @Transactional
    void createExistAccount() {
        accountService.createAccount("userId");
        AccountExistException existException = assertThrows(AccountExistException.class,()->accountService.createAccount("userId"));
        assertEquals("해당 계좌가 이미 존재합니다.",existException.getMessage());
    }

    @Test
    @DisplayName("계좌 삭제 테스트")
    @Transactional
    void deleteAccount() {
        Account account = new Account("userId",0L);
        accountRepository.save(account);
        accountService.deleteAccount("userId");
        boolean existsById = accountRepository.existsById("userId");
        assertEquals(false,existsById);
    }

    @Test
    @DisplayName("계좌 삭제시 예치금이 0원보다 클 때 예외 테스트")
    @Transactional
    void deleteAccountMoreThanZeroException() {
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        BalanceNotZeroException NotZeroException = assertThrows(BalanceNotZeroException.class,()->accountService.deleteAccount("userId"));
        assertEquals("예치금 잔액이 남아있습니다.",NotZeroException.getMessage());
    }

    @Test
    @DisplayName("포인트 조회 테스트")
    @Transactional
    void getPoint() {
        Account account = new Account("userId",10000L);
        accountRepository.save(account);
        PointResponseDto userId = accountService.getPoint("userId");
        assertEquals(10000L,userId.point());
    }

    @Test
    @DisplayName("포인트 조회 예외 테스트(해당 계좌가 존재하지 않을 때)")
    @Transactional
    void getPointNotExistAccountException() {
        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,()->accountService.getPoint("userId"));
        assertEquals("해당 계좌가 없습니다.",accountNotFoundException.getMessage());
    }

    @Test
    void depositPoint() {
    }

    @Test
    void withdrawPoint() {
    }

    @Test
    void getTransactionHistory() {
    }
}