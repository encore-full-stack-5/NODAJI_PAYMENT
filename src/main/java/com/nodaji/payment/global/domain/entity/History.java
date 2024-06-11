package com.nodaji.payment.global.domain.entity;

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
    @Id
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
}