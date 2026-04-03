package com.iprody.ms.order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderLineResponse {
    private Long id;
    private String productName;
    private int quantity;
    private MoneyResponse price;
}
