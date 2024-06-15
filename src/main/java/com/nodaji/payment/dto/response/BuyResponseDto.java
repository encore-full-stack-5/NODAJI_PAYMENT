package com.nodaji.payment.dto.response;

public record BuyResponseDto (String status){
    public static BuyResponseDto from(String status) {
        return new BuyResponseDto(
                status
        );
    }
}
