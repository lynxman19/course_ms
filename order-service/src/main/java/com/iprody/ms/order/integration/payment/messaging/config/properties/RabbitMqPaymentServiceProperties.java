package com.iprody.ms.order.integration.payment.messaging.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.service.payment")
public record RabbitMqPaymentServiceProperties(
        String exchangeRequestName,
        String queueResponseName,
        String queueRequestName
) {
}
