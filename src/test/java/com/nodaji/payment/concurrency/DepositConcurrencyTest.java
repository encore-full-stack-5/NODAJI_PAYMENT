package com.nodaji.payment.concurrency;

import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.global.concurrency.aop.AopForTransaction;
import com.nodaji.payment.global.concurrency.aop.DistributedLockAop;
import com.nodaji.payment.global.concurrency.config.RedissonConfig;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.ExceedsBalanceException;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;



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
        accountRepository.save(new Account(userId,initialPoint));
    }

    @Test
    @DisplayName("출금 동시성 처리 테스트")
    public void testConcurrentWithdrawals() throws InterruptedException {
        WithdrawRequestDto req = new WithdrawRequestDto(withdrawPrice, withdrawCharge,"","");

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
    @DisplayName("출금 동시성 처리 결제금액 > 예치금 예외 테스트")
    public void testWithdrawExceedsBalance() {
        String userId = "user123";
        WithdrawRequestDto req = new WithdrawRequestDto(400000000L, 10L,"","");

        assertThrows(ExceedsBalanceException.class, () -> {
            accountService.withdrawPoint(userId, req);
        });
    }

    @Test
    @DisplayName("출금 동시성 처리 Lock을 못잡을 경우 예외 테스트")
    public void testLockNotAcquired() throws InterruptedException {
        WithdrawRequestDto req = new WithdrawRequestDto(withdrawPrice, withdrawCharge, "", "");

        // Lock을 미리 선점하여 Lock을 못하게 함.
        String lockKey = "LOCK:" + userId;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        executorService.submit(() -> {
            try {
                accountService.withdrawPoint(userId, req);
            } catch (Exception e) {
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        // 잠금을 해제.
        lock.unlock();
        // 아무 것도 출금되지 않아야 함.
        Account account = accountRepository.findById(userId).orElseThrow(AccountNotFoundException::new);
        assertEquals(initialPoint, account.getPoint());
    }
}
