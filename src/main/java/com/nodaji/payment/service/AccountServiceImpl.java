package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.dto.AccountDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public void createAccount(String userId) {
        if(!accountRepository.existsById(userId)) {
            Account newAccount = new Account(userId,0L);
            accountRepository.save(newAccount);
        }
        else throw new IllegalArgumentException("생성되어있는 계좌입니다.");
    }

    @Override
    public void deleteAccount(String userId) {
        if(accountRepository.existsById(userId)) {
            accountRepository.deleteById(userId);
        }
        else throw new IllegalArgumentException("존재하지 않는 계좌입니다.");
    }

    @Override
    public Long getPoint(String userId) {
        if(accountRepository.existsById(userId)){
            return accountRepository.findPointByUserId(userId);
        }
        else throw new IllegalArgumentException("없는 계좌입니다.");
    }
}
