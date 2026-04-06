package com.iprody.ms.delivery.web.dto;

import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DeliveryRequest {
    private Long orderId;
    private DeliveryStatus status;
    private DeliveryAddressRequest deliveryAddress;
    private LocalDate deliveryDate;
    private TimeWindowRequest timeWindow;
    private String trackingNumber;
}
