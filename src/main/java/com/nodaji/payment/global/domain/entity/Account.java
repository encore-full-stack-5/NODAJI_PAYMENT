package com.nodaji.payment.global.domain.entity;

import com.nodaji.payment.global.domain.exception.BalanceNotEnoughException;
import com.nodaji.payment.global.domain.exception.ExceedsBalanceException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ACCOUNTS")
public class Account {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "POINT")
    private Long point;

    public Account toEntity(String userId,Long point){
        return Account.builder()
                .userId(userId)
                .point(point)
                .build();

    }

    public void decreaseBalanceWithCharge(Long price, Long charge) {
        if(point<price+charge) throw new ExceedsBalanceException();
        else point = point-(price+charge);
    }

    public Long increaseBalance(Long amount) {
        return point = point+amount;
    }

    public Long decreaseBalance(Long amount) {
        if(amount > point) throw new BalanceNotEnoughException();
        return point = point-amount;

    }
}