package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;

public interface AccountService {
    void createAccount(String userId);

    void deleteAccount(String userId);

    PointResponseDto getPoint(String userId);

    void depositPoint(String userId, Long req);

    void withdrawPoint(String userId, WithdrawRequestDto req);

    void getTransactionHistory(String userId);
    void createWithdrawHistory(String userId, WithdrawRequestDto req);
    void createDepositHistory(String userId, Long price);
    Boolean isExistAccount(String userId);
}
