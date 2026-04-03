package com.iprody.ms.payment.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentAmountResponse {
    BigDecimal amount;
}
