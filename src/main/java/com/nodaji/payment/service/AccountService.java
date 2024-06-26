package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.global.domain.dto.KafkaAccountDto;
import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.History;

import java.util.Date;
import java.util.List;

public interface AccountService {
    void createAccount(KafkaAccountDto data);
    void createAccount(String userId);

    void deleteAccount(String userId);

    PointResponseDto getPoint(String userId);

    void depositPoint(String userId, Long req);
    void depositWinPoint(String userId,WinDepositDto req);
    void deductPoint(String userId, BuyRequestDto req);
    void withdrawPoint(String userId, WithdrawRequestDto req);


    Boolean isExistAccount(String userId);
    BuyResponseDto buyItems(String userId, BuyRequestDto req);
}
