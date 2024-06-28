package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.global.domain.dto.KafkaBalanceDto;
import com.nodaji.payment.global.kafka.AccountProducer;
import com.nodaji.payment.global.domain.dto.KafkaAccountDto;
import com.nodaji.payment.global.kafka.KafkaStatus;
import com.nodaji.payment.global.domain.dto.WinDepositDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.utils.DistributedLock;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.*;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountProducer accountProducer;
    private final HistoryService historyService;

    /**
     * 계좌 존재 유무 확인
     */
    public Boolean isExistAccount(String userId){
        return accountRepository.existsById(userId);
    }

    /**
     * kafka로 userId를 받아 계좌 생성
     */
    @KafkaListener(topics = "account-topic")
    public void synchronization(KafkaStatus<KafkaAccountDto> status) {
        switch (status.status()) {
            case "createAccount" -> {
                createAccount(status.data());
            }
        }
    }
    /**
     * 계좌 생성
     */
    @Override
    @Transactional
    public void createAccount(KafkaAccountDto data) {
        if(!accountRepository.existsById(data.userId())) {
            accountRepository.save(new Account().toEntity(data.userId(),0L));
        }
        else throw new AccountExistException();
    }

    /**
     * 테스트용 계좌 생성
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
        Long balanceResult = account.decreaseBalance(req.amount());
        accountProducer.send(KafkaBalanceDto.from(balanceResult,userId),"updatePoint");
    }

    /**
     * 예치금 충전
     */
    @Override
    @Transactional
    public void depositPoint(String userId, Long amount) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(AccountNotFoundException::new);
        Long balanceResult = account.increaseBalance(amount);
        accountProducer.send(KafkaBalanceDto.from(balanceResult,userId),"updatePoint");
    }
    /**
     * 예치금 충전(당첨)
     */
    @Override
    @Transactional
    public void depositWinPoint(WinDepositDto req) {
        Account account = accountRepository.findById(req.userId())
                .orElseThrow(AccountNotFoundException::new);
        Long balanceResult = account.increaseBalance(req.amount());
        historyService.createWinDepositHistory(req);
        accountProducer.send(KafkaBalanceDto.from(balanceResult,req.userId()),"updatePoint");
    }
    /**
     * 예치금 출금
     */
    @Override
    @DistributedLock(key = "#userId")
    public void withdrawPoint(String userId, WithdrawRequestDto req) {
        Account account = accountRepository.findById(userId).orElseThrow(AccountNotFoundException::new);
//        출금 메소드 출금하려는 금액이 예치금+수수료보다 많을 때 예외
        account.decreaseBalanceWithCharge(req.price(),req.charge());
//        거래내역에 추가
        historyService.createWithdrawHistory(userId, req);

    }


    /**
     * 구매시
     */
    @Override
    @Transactional
    public BuyResponseDto buyItems(String userId, BuyRequestDto req){
//        예치금 차감
        deductPoint(userId,req);
//        거래내역 기록
        historyService.createBuyHistory(userId,req);
        return BuyResponseDto.from("success");
    }
}
