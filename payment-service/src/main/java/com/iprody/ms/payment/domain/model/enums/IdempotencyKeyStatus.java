package com.iprody.ms.payment.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdempotencyKeyStatus {
    PENDING, COMPLETED;
}
