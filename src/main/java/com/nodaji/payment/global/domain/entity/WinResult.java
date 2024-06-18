package com.nodaji.payment.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    @Column(name = "LEFT_MONTHS")
    private Integer leftMonths;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "USER_ID")
    private String userId;
}
