package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, String> {
    /**
     * jpql 사용이유 : 필요한 값이 하나이기 때문에 객체로 받아와서 꺼내는것 보다 빠를것이라 판단
     */
    @Query("SELECT a.point FROM Account a WHERE a.userId = :userId")
    Long findPointByUserId(@Param("userId") String userId);
}
