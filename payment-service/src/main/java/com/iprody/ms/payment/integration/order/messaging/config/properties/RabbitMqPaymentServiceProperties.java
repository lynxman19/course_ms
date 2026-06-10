package com.iprody.ms.payment.integration.order.messaging.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.service.order")
public record RabbitMqPaymentServiceProperties(
        String exchangeResponseName,
        String queueResponseName,
        String queueRequestName
) {
}
