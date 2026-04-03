package com.iprody.ms.delivery.web.dto;

import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DeliveryResponse {
    private Long id;
    private Long orderId;
    private DeliveryStatus status;
    private DeliveryAddressResponse deliveryAddress;
    private LocalDate deliveryDate;
    private TimeWindowResponse timeWindow;
    private String trackingNumber;
}
