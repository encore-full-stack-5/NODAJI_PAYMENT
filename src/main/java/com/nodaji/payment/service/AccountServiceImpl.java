package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.global.domain.dto.AccountDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public void createAccount(String userId) {
        if(!accountRepository.existsById(userId)) {
            accountRepository.save(new Account().toEntity(userId,0L));
        }
        else throw new IllegalArgumentException("생성되어있는 계좌입니다.");
    }

    @Override
    public void deleteAccount(String userId) {
        if(accountRepository.existsById(userId)) accountRepository.deleteById(userId);
        else throw new IllegalArgumentException("존재하지 않는 계좌입니다.");
    }

    @Override
    public Long getPoint(String userId) {
        if(accountRepository.existsById(userId)) return accountRepository.findPointByUserId(userId);
        else throw new IllegalArgumentException("없는 계좌입니다.");
    }

    @Override
    @Transactional
    public void depositPoint(DepositRequestDto req) {
        Account account = accountRepository.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 ID를 찾을 수 없습니다: " + req.userId()));
        account.setPoint(account.getPoint()+req.price());
        accountRepository.save(account);
    }
}
