package com.iprody.ms.order.integration.delivery.messaging.dto;

import java.math.BigDecimal;

public record OrderPaidMessage (
        Long orderId,
        BigDecimal amount
) {
}
