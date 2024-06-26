package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;
    /**
     * 입금 거래내역 추가
     */
    @Override
    public void createDepositHistory(String userId, Long price) {
        historyRepository.save(History.toEntity(userId, price));
    }
    /**
     * 당첨금 입금내역 추가
     */
    @Override
    public void createWinDepositHistory(String userId, WinDepositDto req) {
        historyRepository.save(req.toEntity(userId, req));
    }
    /**
     * 출금 거래내역 추가
     */
    @Override
    public void createWithdrawHistory(String userId, WithdrawRequestDto req) {
        historyRepository.save(req.toEntity(userId, req));
    }

    /**
     * 결제 거래내역 추가
     */
    @Override
    public void createBuyHistory(String userId, BuyRequestDto req) {
        historyRepository.save(BuyRequestDto.toEntity(userId, req));
    }

    /**
     * 예치금 거래내역 조회
     */
    @Override
    public List<History> getTransactionHistory(String userId, Date startDate, Date endDate) {
        return historyRepository.findHistoryByUserIdAndDateRange(userId,startDate,endDate);
    }

    /**
     * 예치금 출금 조회
     */
    @Override
    public List<History> getWithdrawHistory(String userId, Date startDate, Date endDate) {
        return historyRepository.findWithdrawHistoryByUserIdAndDateRange(userId,startDate,endDate);
    }
}
