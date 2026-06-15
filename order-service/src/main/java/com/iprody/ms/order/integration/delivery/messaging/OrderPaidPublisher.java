package com.iprody.ms.order.integration.delivery.messaging;

import com.iprody.ms.order.domain.model.aggregate.Order;
import com.iprody.ms.order.integration.delivery.messaging.config.KafkaDeliveryProperties;
import com.iprody.ms.order.integration.delivery.messaging.dto.OrderPaidMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidPublisher {
    private final KafkaTemplate<String, OrderPaidMessage> kafkaTemplate;
    private final KafkaDeliveryProperties props;

    public void publish(Order order) {
        var message = new OrderPaidMessage(
                order.getOrderId(),
                order.getTotalPrice().getPrice()
        );
        kafkaTemplate.send(props.orderPaidTopic(), order.getOrderId().toString(), message);
        log.info("Published OrderPaidMessage for orderId={}", order.getOrderId());
    }
}
