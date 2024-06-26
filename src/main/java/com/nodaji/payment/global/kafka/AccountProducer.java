package com.nodaji.payment.global.kafka;

import com.nodaji.payment.global.domain.dto.KafkaAccountDto;
import com.nodaji.payment.global.domain.dto.KafkaBalanceDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProducer {
    private final KafkaTemplate<String, KafkaStatus<KafkaBalanceDto>> kafkaTemplate;
    String name ="account-topic";

    @Bean
    private NewTopic newTopic(){
        return new NewTopic(name, 1, (short) 1);
    }

    public void send(KafkaBalanceDto kafkaBalanceDto, String status){
        KafkaStatus<KafkaBalanceDto> kafkaStatus = new KafkaStatus<>(kafkaBalanceDto, status);
        kafkaTemplate.send(name, kafkaStatus);
    }

}