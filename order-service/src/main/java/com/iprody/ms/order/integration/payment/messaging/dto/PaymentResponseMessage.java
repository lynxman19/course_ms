package com.iprody.ms.order.integration.payment.messaging.dto;

public record PaymentResponseMessage(
        Long orderId,
        Long paymentId,
        String status
) {
}
