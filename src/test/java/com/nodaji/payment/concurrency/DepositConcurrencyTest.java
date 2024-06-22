package com.nodaji.payment.concurrency;

import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.global.concurrency.config.AopForTransaction;
import com.nodaji.payment.global.concurrency.config.DistributedLockAop;
import com.nodaji.payment.global.concurrency.config.RedissonConfig;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.ExceedsBalanceException;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.service.AccountServiceImpl;
import com.nodaji.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Import({RedissonConfig.class, DistributedLockAop.class, AopForTransaction.class})
public class DepositConcurrencyTest {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RedissonClient redissonClient;

    String userId = "user123";
    Long initialPoint = 1_000_000L;
    Long withdrawPrice = 10000L;
    Long withdrawCharge = 8000L;
    @BeforeEach
    public void setup() {
        // 테스트용 계정 설정
        Account account = new Account();
        accountRepository.save(new Account(userId,initialPoint));
    }

    @Test
    public void testConcurrentWithdrawals() throws InterruptedException {
        WithdrawRequestDto req = new WithdrawRequestDto(withdrawPrice, withdrawCharge,"","","");

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulWithdrawals = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.withdrawPoint(userId, req);
                    successfulWithdrawals.incrementAndGet();
                } catch (Exception e) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Account account = accountRepository.findById(userId).orElseThrow(AccountNotFoundException::new);
        assertEquals(10000L,accountRepository.findByUserId("user123").getPoint());
        assertEquals(successfulWithdrawals.get(), initialPoint / (withdrawPrice + withdrawCharge));
    }

    @Test
    public void testWithdrawExceedsBalance() {
        String userId = "user123";
        WithdrawRequestDto req = new WithdrawRequestDto(400000000L, 10L,"","","");

        assertThrows(ExceedsBalanceException.class, () -> {
            accountService.withdrawPoint(userId, req);
        });
    }
}