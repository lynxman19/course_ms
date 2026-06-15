package com.iprody.ms.order.integration.delivery.messaging.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaDeliveryProperties.class)
public class KafkaDeliveryConfig {
}
