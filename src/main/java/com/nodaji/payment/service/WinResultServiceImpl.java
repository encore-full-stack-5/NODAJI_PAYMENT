package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.WinRequestDto;
import com.nodaji.payment.global.domain.repository.WinResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WinResultServiceImpl implements WinResultService{
    private final WinResultRepository winResultRepository;
    private final AccountService accountService;
    @Transactional
    public void winResultProcess(String userId, WinRequestDto req){
        if(req.type().equals("연금복권")) winResultRepository.save(req.toEntity(userId,req));
        else accountService.depositPoint(userId,req.amount());
    }
}
