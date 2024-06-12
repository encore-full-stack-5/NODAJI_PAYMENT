package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.dto.AccountDto;

public interface AccountService {
    void createAccount(String userId);

    void deleteAccount(String userId);

    Long getPoint(String userId);
}
