package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.AccountExistException;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
        Account account = new Account("userId",0L);
        accountRepository.save(account);
        Account byUserId = accountRepository.findByUserId("userId");
        assertEquals(0L, byUserId.getPoint());
        assertEquals("userId", byUserId.getUserId());
    }

    @Test
    @DisplayName("계좌 중복 생성 예외 테스트")
    @Transactional
    void createExistAccount() {
        Account account = new Account("userId",0L);
        accountRepository.save(account);
        AccountExistException existException = assertThrows(AccountExistException.class,()->accountService.createAccount("userId"));
        assertEquals("해당 계좌가 이미 존재합니다.",existException.getMessage());
    }




    @Test
    void deleteAccount() {
    }

    @Test
    void getPoint() {
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