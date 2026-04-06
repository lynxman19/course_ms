package com.iprody.ms.order.service.execute;

import com.iprody.ms.order.domain.model.valueobjects.OrderStatus;
import com.iprody.ms.order.service.dto.AddressDto;
import com.iprody.ms.order.service.dto.OrderLineDto;

import java.util.List;

public record OrderExecute (
        Long customerId,
        OrderStatus status,
        AddressDto address,
        List<OrderLineDto> lines

) {
}
