package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.WinResult;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.global.domain.repository.WinResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ScheduledDepositServiceTest {

    @Autowired
    ScheduledDepositService scheduledDepositService;

    @Autowired
    WinResultRepository winResultRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository.saveAll(List.of(
                new Account("userId1", 0L),
                new Account("userId2", 0L),
                new Account("userId3", 0L)
        ));

        winResultRepository.saveAll(List.of(
                new WinResult(0L, 3, 100_000L, "userId1"),
                new WinResult(0L, 2, 1_000_000L, "userId2"),
                new WinResult(0L, 1, 10_000_000L, "userId3")
        ));
    }


    @Test
    void addPensionAmountToDeposit() {
        // given
        scheduledDepositService.addPensionAmountToDeposit();

        // when
        List<WinResult> allValidWinResults = winResultRepository.findAllValidWinResults();
        assertEquals(2, allValidWinResults.size());

        Account account1 = accountRepository.findByUserId("userId1");
        Account account2 = accountRepository.findByUserId("userId2");
        Account account3 = accountRepository.findByUserId("userId3");

        // then
        assertAll(
                () -> assertEquals(100_000L, account1.getPoint()),
                () -> assertEquals(1_000_000L, account2.getPoint()),
                () -> assertEquals(10_000_000L, account3.getPoint())
        );

        // 남은 개월 수 검증
        allValidWinResults.forEach(winResult -> {
            if (winResult.getUserId().equals("userId1")) {
                assertEquals(2, winResult.getLeftMonths());
            } else if (winResult.getUserId().equals("userId2")) {
                assertEquals(1, winResult.getLeftMonths());
            }
        });
    }
}