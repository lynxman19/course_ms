package com.iprody.ms.delivery.integration.order.messaging.dto;

import java.math.BigDecimal;

public record OrderPaidMessage(
        Long orderId,
        BigDecimal amount
        ) {
}
