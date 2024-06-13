package com.nodaji.payment.dto.response;

import com.nodaji.payment.global.domain.entity.Account;

public record PointResponseDto (Long point){
    public static PointResponseDto from(Account account) {
        return new PointResponseDto(
                account.getPoint()
        );
    }
}
