package com.iprody.ms.payment.web.dto;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private PaymentStatus status;
    private PaymentMethod method;
    private PaymentAmountResponse amount;
    private LocalDateTime createdAt;
}
