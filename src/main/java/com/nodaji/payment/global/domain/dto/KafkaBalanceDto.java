package com.nodaji.payment.global.domain.dto;

public record KafkaBalanceDto(Long balance) {
    public static KafkaBalanceDto from(Long point) {
        return new KafkaBalanceDto(
                point
        );
    }
}
