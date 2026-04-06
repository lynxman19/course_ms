package com.iprody.ms.order.web.dto;

import com.iprody.ms.order.domain.model.valueobjects.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long customerId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private AddressResponse addressResponse;
    private MoneyResponse amount;
    private List<OrderLineResponse> orderLines;
}
