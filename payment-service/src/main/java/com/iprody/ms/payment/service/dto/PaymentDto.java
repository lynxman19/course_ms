package com.iprody.ms.payment.service.dto;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Long orderId,
        PaymentStatus status,
        PaymentMethod method,
        PaymentAmountDto amount,
        LocalDateTime createdAt
) {
}
