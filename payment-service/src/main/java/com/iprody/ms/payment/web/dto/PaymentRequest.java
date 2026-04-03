package com.iprody.ms.payment.web.dto;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private PaymentStatus status;
    private PaymentMethod method;
    private PaymentAmountRequest amount;
}
