package com.nodaji.payment.global.domain.dto;

import java.util.Date;

public record HistoryDto(Long id, String transaction_status, Long price, Date created_at) {
}
