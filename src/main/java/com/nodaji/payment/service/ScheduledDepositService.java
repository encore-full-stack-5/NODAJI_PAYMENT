package com.nodaji.payment.service;

import com.nodaji.payment.global.domain.entity.WinResult;
import com.nodaji.payment.global.domain.repository.WinResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledDepositService {

    private final WinResultRepository winResultRepository;
    private final AccountService accountService;
    @Transactional
    public void addPensionAmountToDeposit(){
        List<WinResult> allValidWinResults = winResultRepository.findAll();
        allValidWinResults.forEach(result -> {
            accountService.depositPoint(result.getUserId(), result.getAmount());
            result.setLeftMonths(result.getLeftMonths() - 1);
            winResultRepository.save(result);
        });
    }
}
