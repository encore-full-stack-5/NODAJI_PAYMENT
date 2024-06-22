package com.nodaji.payment.global.concurrency.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.nodaji.payment.global.concurrency.config.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Method signature: {}", signature.toLongString());

        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        if (distributedLock == null) {
            throw new IllegalArgumentException("DistributedLock annotation is missing");
        }

        String[] parameterNames = new String[]{"userId", "amount"};
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(parameterNames, joinPoint.getArgs(), distributedLock.key());

        RLock rLock = redissonClient.getLock(key);
        log.info("Trying to acquire lock: {}", key);

        boolean acquired = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
        if (!acquired) {
            log.warn("Could not acquire lock: {}", key);
            return null; // 락을 획득하지 못한 경우 null 반환
        }

        try {
            log.info("Lock acquired: {}", key);
            return aopForTransaction.proceed(joinPoint);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.info("Lock released: {}", key);
            }
        }
    }
}