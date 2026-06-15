package com.iprody.ms.delivery.integration.order.messaging.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaOrderProperties.class)
public class KafkaOrderConfig {
}
