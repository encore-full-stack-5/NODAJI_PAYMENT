package com.nodaji.payment.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ACCOUNTS"
//        ,indexes = {@Index(columnList = "CATEGORY_ID")}
)
public class Account {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "POINT")
    @Setter
    private Long point;

    public Account toEntity(String userId,Long point){
        return Account.builder()
                .userId(userId)
                .point(point)
                .build();
    }
}