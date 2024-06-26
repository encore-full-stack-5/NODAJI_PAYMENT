package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;
import java.util.List;

public interface HistoryService {
    List<History> getTransactionHistory(String userId, Date startDate, Date endDate);
    List<History> getWithdrawHistory(String userId, Date startDate, Date endDate);
    void createWithdrawHistory(String userId, WithdrawRequestDto req);

    void createDepositHistory(String userId, Long price);
    void createWinDepositHistory(String userId, WinDepositDto req);
    void createBuyHistory(String userId, BuyRequestDto req);
}
