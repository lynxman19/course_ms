package com.iprody.ms.order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MoneyRequest {
    private BigDecimal amount;
}
