package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.dto.response.PointResponseDto;
import com.nodaji.payment.global.domain.dto.AccountDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.exception.AccountExistException;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.exception.BalanceNotZeroException;
import com.nodaji.payment.global.domain.exception.UserNotFoundException;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    /**
     * 계좌 생성
     */
    @Override
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
     * 예치금 충전
     */
    @Override
    @Transactional
    public void depositPoint(String userId, DepositRequestDto req) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        account.setPoint(account.getPoint()+req.price());
        accountRepository.save(account);
    }
    /**
     * 예치금 출금
     */
    @Override
    public void withdrawPoint(String userId, DepositRequestDto req) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        account.setPoint(account.getPoint()-req.price());
        accountRepository.save(account);
    }

    @Override
    public void getTransactionHistory(String userId) {
        // 거래내역을 가져오는 메소드
    }
}
