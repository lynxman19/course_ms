package com.iprody.ms.order.web.dto;

import com.iprody.ms.order.domain.model.valueobjects.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private OrderStatus status;
    private AddressRequest address;
    private List<OrderLineRequest> orderLines = new ArrayList<>();
}
