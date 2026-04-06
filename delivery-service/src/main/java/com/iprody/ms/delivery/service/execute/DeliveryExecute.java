package com.iprody.ms.delivery.service.execute;

import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;
import com.iprody.ms.delivery.service.dto.DeliveryAddressDto;
import com.iprody.ms.delivery.service.dto.TimeWindowDto;

import java.time.LocalDate;

public record DeliveryExecute(
        Long orderId,
        DeliveryStatus status,
        DeliveryAddressDto deliveryAddress,
        LocalDate deliveryDate,
        TimeWindowDto timeWindow,
        String trackingNumber
) {
}
