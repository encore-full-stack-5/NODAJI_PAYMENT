package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory,Long> {
}
