package com.iprody.ms.payment.service.execute;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;
import com.iprody.ms.payment.service.dto.PaymentAmountDto;

public record PaymentExecute(
        Long orderId,
        PaymentStatus status,
        PaymentMethod method,
        PaymentAmountDto amount
) {
}
