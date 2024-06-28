package com.nodaji.payment.global.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "PAYMENT_HISTORY")
public class PaymentHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long historyId;
    @Column(name = "ORDER_ID")
    private String orderId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "METHOD")
    private String method;
    @Column(name = "APPROVED_AT")
    private String approvedAt;
    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "ORDER_NAME")
    private String orderName;
    @Column(name = "STATUS")
    private String status;

    public PaymentHistory toEntity(JSONObject jsonObject, String userId){
        return PaymentHistory.builder()
                .historyId(null)
                .userId(userId)
                .orderId(jsonObject.get("orderId").toString())
                .type(jsonObject.get("type").toString())
                .method(jsonObject.get("method").toString())
                .approvedAt(jsonObject.get("approvedAt").toString())
                .totalAmount(jsonObject.get("totalAmount").toString())
                .status(jsonObject.get("status").toString())
                .orderName(jsonObject.get("orderName").toString())
                .build();

    }
}