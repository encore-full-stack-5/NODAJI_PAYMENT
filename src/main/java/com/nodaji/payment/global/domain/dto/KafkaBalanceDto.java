package com.nodaji.payment.global.domain.dto;

public record KafkaBalanceDto(Long balance, String userId) {
    public static KafkaBalanceDto from(Long point,String userId) {
        return new KafkaBalanceDto(
                point,userId
        );
    }
}
