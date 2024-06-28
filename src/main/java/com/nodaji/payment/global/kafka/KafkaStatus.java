package com.nodaji.payment.global.kafka;

public record KafkaStatus<T>(
        T data, String status

) {
}