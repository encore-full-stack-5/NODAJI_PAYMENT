package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    @Query("SELECT h FROM History h WHERE h.userId = :userId AND h.createdAt BETWEEN :startDate AND :endDate ORDER BY h.createdAt DESC")
    List<History> findHistoryByUserIdAndDateRange(@Param("userId") String userId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    @Query("SELECT h FROM History h WHERE h.transactionStatus = '출금' AND h.userId = :userId AND h.createdAt BETWEEN :startDate AND :endDate ORDER BY h.createdAt DESC")
    List<History> findWithdrawHistoryByUserIdAndDateRange(@Param("userId") String userId,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);
}
