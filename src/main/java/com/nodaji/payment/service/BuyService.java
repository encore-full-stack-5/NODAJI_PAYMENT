package com.nodaji.payment.service;

import com.nodaji.payment.dto.request.BuyRequestDto;
import com.nodaji.payment.dto.response.BuyResponseDto;

public interface BuyService {
    BuyResponseDto buyItems(String userId, BuyRequestDto req);
}
