package com.nodaji.payment.global.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public record AccountDto(String user_id, Long point) {
}
