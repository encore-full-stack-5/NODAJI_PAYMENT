package com.nodaji.payment.global.domain.entity;

import com.nodaji.payment.dto.request.DepositRequestDto;
import com.nodaji.payment.dto.request.WithdrawRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "HISTORY"
//        ,indexes = {@Index(columnList = "CATEGORY_ID")}
)
public class History {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long id;
    @Column(name = "TRANSACTION_STATUS")
    private String transaction_status;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "CREATED_AT")
    private Date created_at;
    @Column(name = "USER_ID")
    private String userId;

    public History toEntity(String userId, WithdrawRequestDto req){
        return History.builder()
                .transaction_status("출금")
                .price(req.price()+req.charge())
                .created_at(new Date())
                .userId(userId)
                .build();
    }
    public History toEntity(String userId, Long price){
        return History.builder()
                .transaction_status("입금")
                .price(price)
                .created_at(new Date())
                .userId(userId)
                .build();
    }

}