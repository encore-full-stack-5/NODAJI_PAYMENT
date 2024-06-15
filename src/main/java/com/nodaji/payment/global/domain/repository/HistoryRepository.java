package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findByUserIdOrderByCreatedAtDesc(String userId);
}
