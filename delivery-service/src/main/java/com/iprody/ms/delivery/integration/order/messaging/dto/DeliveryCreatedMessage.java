package com.iprody.ms.delivery.integration.order.messaging.dto;

public record DeliveryCreatedMessage(
        Long orderId,
        Long deliveryId,
        String status
) {
}
