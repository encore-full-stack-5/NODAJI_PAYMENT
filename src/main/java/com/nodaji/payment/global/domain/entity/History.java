package com.nodaji.payment.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "HISTORY")
public class History {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long id;
    @Column(name = "TRANSACTION_STATUS")
    private String transactionStatus;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "WITHDRAW_BANK_NAME")
    private String withdrawBankName;
    @Column(name = "ACCOUNT_NUM")
    private String withdrawAccountNum;
    

    public static History toEntity(String userId, Long price){
        return History.builder()
                .transactionStatus("입금")
                .price(price)
                .createdAt(new Date())
                .userId(userId)
                .withdrawAccountNum(null)
                .withdrawBankName(null)
                .build();
    }


}