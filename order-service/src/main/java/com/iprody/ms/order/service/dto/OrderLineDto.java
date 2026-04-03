package com.iprody.ms.order.service.dto;

import java.util.List;

public record OrderLineDto(
        Long id,
        String productName,
        int quantity,
        MoneyDto price
) {
}
