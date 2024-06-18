package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.WinRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.WinResult;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.global.domain.repository.WinResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WinResultServiceImplTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    WinResultRepository winResultRepository;
    @Autowired
    WinResultServiceImpl winResultService;
    @Autowired
    AccountServiceImpl accountService;
    String userId = "testUserId";

    @BeforeEach
    void beforeEach() {
        winResultRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account(userId, 10000L);
        accountRepository.save(account);
    }


    @Test
    @DisplayName("동행복권 당첨 테스트")
    void winLottoProcess() {
        // given
        winResultService.winResultProcess(userId,new WinRequestDto("동행복권",0,100_000_000L));
        // when
        PointResponseDto getPoint = accountService.getPoint(userId);
        // then
        assertEquals(100_010_000L,getPoint.point());
    }

    @Test
    @DisplayName("토토 당첨 테스트")
    void winTotoProcess() {
        // given
        winResultService.winResultProcess(userId,new WinRequestDto("토토",0,100_000_000L));
        // when
        PointResponseDto getPoint = accountService.getPoint(userId);
        // then
        assertEquals(100_010_000L,getPoint.point());
    }

    @Test
    @DisplayName("연금복권 당첨 테스트")
    void winPensionProcess() {
        // given
        winResultService.winResultProcess(userId,new WinRequestDto("연금복권",10,1_000_000L));
        // when
        WinResult byUserId = winResultRepository.findByUserId(userId);
        // then
        assertEquals(10,byUserId.getLeftMonths());
        assertEquals(1_000_000L,byUserId.getAmount());
        assertEquals(userId,byUserId.getUserId());
    }
}