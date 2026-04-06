package com.iprody.ms.payment.web.mapper;

import com.iprody.ms.payment.service.dto.PaymentAmountDto;
import com.iprody.ms.payment.service.dto.PaymentDto;
import com.iprody.ms.payment.service.execute.PaymentExecute;
import com.iprody.ms.payment.web.dto.PaymentAmountRequest;
import com.iprody.ms.payment.web.dto.PaymentAmountResponse;
import com.iprody.ms.payment.web.dto.PaymentRequest;
import com.iprody.ms.payment.web.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse mapToPaymentResponse(PaymentDto paymentDto) {
        return new PaymentResponse(
                paymentDto.id(),
                paymentDto.orderId(),
                paymentDto.status(),
                paymentDto.method(),
                mapToPaymentAmountResponse(paymentDto.amount()),
                paymentDto.createdAt()
        );
    }

    public PaymentExecute mapToExecutePayment(PaymentRequest paymentRequest) {
        return new PaymentExecute(
                paymentRequest.getOrderId(),
                paymentRequest.getStatus(),
                paymentRequest.getMethod(),
                mapToPaymentAmountDto(paymentRequest.getAmount())
        );
    }

    private PaymentAmountResponse mapToPaymentAmountResponse(PaymentAmountDto paymentAmountDto) {
        return new PaymentAmountResponse(paymentAmountDto.amount());
    }

    private PaymentAmountDto mapToPaymentAmountDto(PaymentAmountRequest paymentAmountRequest) {
        if (paymentAmountRequest == null) {
            return null;
        }
        return new PaymentAmountDto(paymentAmountRequest.getAmount());
    }
}
