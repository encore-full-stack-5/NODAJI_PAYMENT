package com.nodaji.payment.global.domain.dto;

import com.nodaji.payment.global.domain.entity.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

public record AccountDto(String user_id, Long point) {

}
