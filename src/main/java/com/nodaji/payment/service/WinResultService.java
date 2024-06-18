package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.WinRequestDto;

public interface WinResultService {
    void winResultProcess(String userId, WinRequestDto req);
}
