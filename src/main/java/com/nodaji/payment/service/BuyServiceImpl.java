package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.BalanceNotEnoughException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BuyServiceImpl implements BuyService{
    private final AccountService accountService;

    @Transactional
    public BuyResponseDto buyItems(String userId,BuyRequestDto req){
//        예치금 차감
        accountService.deductPoint(userId,req);
//        거래내역 기록
        accountService.createBuyHistory(userId,req);
        return BuyResponseDto.from("success");
    }
}
