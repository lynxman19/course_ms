package com.iprody.ms.order.integration.payment.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.iprody.ms.order.integration.payment.messaging.dto.PaymentResponseMessage;

@Component
@Slf4j
public class PaymentResponseListener {
    @RabbitListener(queues = "${rabbitmq.service.payment.queue-response-name}")
    public void handle(PaymentResponseMessage message) {
        log.info("Received order payment response: orderId={}, paymentId={}, status={}",
                message.orderId(), message.paymentId(), message.status());
    }

}
