package com.iprody.ms.order.integration.payment.dto.request;

import com.iprody.ms.order.integration.payment.dto.common.PaymentAmount;
import com.iprody.ms.order.integration.payment.dto.common.PaymentMethod;
import com.iprody.ms.order.integration.payment.dto.common.PaymentStatus;

public record PaymentRequest (
    Long orderId,
    PaymentStatus status,
    PaymentMethod method,
    PaymentAmount amount
) {}
