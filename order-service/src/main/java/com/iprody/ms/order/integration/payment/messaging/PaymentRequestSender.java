package com.iprody.ms.order.integration.payment.messaging;

import com.iprody.ms.order.domain.model.aggregate.Order;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.messaging.config.properties.RabbitMqPaymentServiceProperties;
import com.iprody.ms.order.integration.payment.messaging.dto.PaymentRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestSender {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqPaymentServiceProperties props;

    public void send(Order order, PaymentRequest paymentRequest) {
        PaymentRequestMessage message =
                new PaymentRequestMessage(
                        order.getOrderId(),
                        order.getTotalPrice().getPrice(),
                        paymentRequest.method().name()
                );

        rabbitTemplate.convertAndSend(props.exchangeRequestName(), props.queueRequestName(), message);
        log.info("Sent payment request message: orderId={}, amount={}", order.getOrderId(),
                order.getTotalPrice().getPrice());
    }
}
