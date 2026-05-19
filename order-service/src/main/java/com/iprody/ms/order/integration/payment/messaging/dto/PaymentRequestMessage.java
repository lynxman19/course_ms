package com.iprody.ms.order.integration.payment.messaging.dto;

import java.math.BigDecimal;

public record PaymentRequestMessage(
        Long orderId,
        BigDecimal amount,
        String method
) {
}
