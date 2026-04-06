package com.iprody.ms.order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineRequest {
    private int quantity;
    private String productName;
    private MoneyRequest price;
}
