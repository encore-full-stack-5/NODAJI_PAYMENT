package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.WinResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WinResultRepository extends JpaRepository<WinResult, Long> {

    WinResult findByUserId(String userId);
    @Query("SELECT w FROM WinResult w WHERE w.leftMonths > 0")
    List<WinResult> findAllValidWinResults();
}
