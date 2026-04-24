package com.iprody.ms.order.integration.payment.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentAmount {
    BigDecimal amount;
}
