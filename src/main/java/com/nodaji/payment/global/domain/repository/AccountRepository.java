package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByUserId(String userId);

}
