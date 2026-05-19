package com.iprody.ms.payment.integration.order.messaging.dto;

public record PaymentResponseMessage(
        Long orderId,
        Long paymentId,
        String status

) {
}
