package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Long> {
}
