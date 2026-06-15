package com.iprody.ms.delivery.integration.order.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.order")
public record KafkaOrderProperties(
        String orderPaidTopic,
        String deliveryCreatedTopic
) {
}
