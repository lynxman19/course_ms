package com.iprody.ms.payment.integration;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;
import com.iprody.ms.payment.integration.order.messaging.config.properties.RabbitMqPaymentServiceProperties;
import com.iprody.ms.payment.integration.order.messaging.dto.PaymentRequestMessage;
import com.iprody.ms.payment.integration.order.messaging.dto.PaymentResponseMessage;
import com.iprody.ms.payment.service.PaymentService;
import com.iprody.ms.payment.service.dto.PaymentAmountDto;
import com.iprody.ms.payment.service.dto.PaymentDto;
import com.iprody.ms.payment.service.execute.PaymentExecute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Слушатель сообщений для для оплаты заказа
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestListener {
    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqPaymentServiceProperties properties;

    /**
     * Обрабатывает входящее сообщение с данными для оплаты заказа
     *
     * @param message сообщение с данными об оплаты заказа
     */
    @RabbitListener(queues = "${rabbitmq.service.order.queue-request-name}")
    public void handle(PaymentRequestMessage message) {
        log.info("Received payment request: orderId = {}, amount = {}, method={}",
                message.orderId(), message.amount(), message.method());

        PaymentExecute execute = new PaymentExecute(
                message.orderId(),
                PaymentStatus.PENDING,
                PaymentMethod.valueOf(message.method()),
                new PaymentAmountDto(message.amount())
        );

        // Вызов сервиса для оплаты
        PaymentDto paymentDto = paymentService.create(execute);

        // Формирование ответного сообщения
        PaymentResponseMessage response = new PaymentResponseMessage(
                paymentDto.orderId(),
                paymentDto.id(),
                paymentDto.status().name()
        );

        // Отправка ответа с результатом оплаты
        rabbitTemplate.convertAndSend(
                properties.exchangeResponseName(),
                properties.queueResponseName(),
                response
        );
        log.info("Sent order payment response message for orderId = {}, paymentId = {}, status = {}",
                response.orderId(), response.paymentId(), response.status());
    }
}
