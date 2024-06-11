package com.nodaji.payment.global.domain.repository;

import com.nodaji.payment.global.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
