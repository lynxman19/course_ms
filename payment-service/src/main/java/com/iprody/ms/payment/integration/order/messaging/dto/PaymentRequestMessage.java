package com.iprody.ms.payment.integration.order.messaging.dto;

import java.math.BigDecimal;

public record PaymentRequestMessage(
        Long orderId,
        BigDecimal amount,
        String method

) {
}
