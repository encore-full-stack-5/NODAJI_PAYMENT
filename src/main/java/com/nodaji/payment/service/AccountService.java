package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.dto.AccountDto;

public interface AccountService {
    void createAccount(String userId);

    void deleteAccount(String userId);

    PointResponseDto getPoint(String userId);

    void depositPoint(String userId, DepositRequestDto req);

    void withdrawPoint(String userId, DepositRequestDto req);

    void getTransactionHistory(String userId);
}
