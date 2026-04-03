package com.iprody.ms.order.service.dto;

import com.iprody.ms.order.domain.model.valueobjects.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto (
    Long id,
    Long customerId,
    OrderStatus status,
    LocalDateTime createdAt,
    AddressDto address,
    MoneyDto totalAmount,
    List<OrderLineDto> orderLines
) {
}
