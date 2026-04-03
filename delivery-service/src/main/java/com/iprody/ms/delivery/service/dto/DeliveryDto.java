package com.iprody.ms.delivery.service.dto;

import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;

import java.time.LocalDate;

public record DeliveryDto(
        Long id,
        Long orderId,
        DeliveryStatus status,
        DeliveryAddressDto deliveryAddress,
        LocalDate deliveryDate,
        TimeWindowDto timeWindow,
        String trackingNumber
) {
}
