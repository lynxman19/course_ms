package com.iprody.ms.order.integration.payment.dto.response;

import com.iprody.ms.order.integration.payment.dto.common.PaymentAmount;
import com.iprody.ms.order.integration.payment.dto.common.PaymentMethod;
import com.iprody.ms.order.integration.payment.dto.common.PaymentStatus;
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
    private PaymentAmount amount;
    private LocalDateTime createdAt;
}
