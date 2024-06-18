package com.nodaji.payment.global.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "WIN_RESULT")
public class WinResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESULT_ID")
    private Long resultId;
    @Column(name = "LEFT_MONTHS")
    private Integer leftMonths;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "USER_ID")
    private String userId;
}
