package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.History;
import com.nodaji.payment.global.domain.exception.*;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.global.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;

    /**
     * 계좌 존재 유무 확인
     */
    public Boolean isExistAccount(String userId){
        return accountRepository.existsById(userId);
    }

    /**
     * 계좌 생성
     */
    @Override
    @Transactional
    public void createAccount(String userId) {
        if(!accountRepository.existsById(userId)) {
            accountRepository.save(new Account().toEntity(userId,0L));
        }
        else throw new AccountExistException();
    }

    /**
     * 계좌 삭제
     */
    @Override
    @Transactional
    public void deleteAccount(String userId) {
        Account account = accountRepository.findById(userId).orElseThrow(AccountNotFoundException::new);
        if(account.getPoint()>0) throw new BalanceNotZeroException();
        else accountRepository.deleteById(userId);
    }

    /**
     * 예치금 조회
     */
    @Override
    public PointResponseDto getPoint(String userId) {
        if(accountRepository.existsById(userId)) {
            Account byUserId = accountRepository.findByUserId(userId);
            return PointResponseDto.from(byUserId);
        }
        else throw new AccountNotFoundException();
    }

    /**
     * 결제시 예치금 차감
     */
    @Override
    @Transactional
    public void deductPoint(String userId, BuyRequestDto req){
        Account account = accountRepository.findById(userId).orElseThrow(AccountNotFoundException::new);
        if(req.amount() > account.getPoint()) throw new BalanceNotEnoughException();
        account.setPoint(account.getPoint()-req.amount());
        accountRepository.save(account);
    }

    /**
     * 예치금 충전
     */
    @Override
    @Transactional
    public void depositPoint(String userId, Long amount) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        account.setPoint(account.getPoint()+amount);
        accountRepository.save(account);
    }
    /**
     * 예치금 출금
     */
    @Override
    @Transactional
    public void withdrawPoint(String userId, WithdrawRequestDto req) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//        출금하려는 금액이 예치금+수수료보다 많을 때 예외
        if(account.getPoint()<(req.price()+req.charge())) throw new ExceedsBalanceException();
        account.setPoint(account.getPoint()-(req.price()+req.charge()));
        accountRepository.save(account);
//        거래내역에 추가
        createWithdrawHistory(userId, req);
    }
    /**
     * 입금 거래내역 추가
     */
    @Override
    public void createDepositHistory(String userId, Long req) {
        historyRepository.save(new History().toEntity(userId, req));
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
    public List<History> getTransactionHistory(String userId) {
        return historyRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
