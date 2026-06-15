package com.iprody.ms.delivery.integration.order.messaging;

import com.iprody.ms.delivery.domain.repository.DeliveryRepository;
import com.iprody.ms.delivery.integration.order.messaging.config.KafkaOrderProperties;
import com.iprody.ms.delivery.integration.order.messaging.dto.DeliveryCreatedMessage;
import com.iprody.ms.delivery.integration.order.messaging.dto.OrderPaidMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.iprody.ms.delivery.domain.model.aggregate.Delivery;
import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;
import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryAddress;
import com.iprody.ms.delivery.domain.model.valueobjects.TimeWindow;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidListener {
    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, DeliveryCreatedMessage> kafkaTemplate;
    private final KafkaOrderProperties props;

    @KafkaListener(topics = "${kafka.order.order-paid-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void custom(OrderPaidMessage message) {
        log.info("Received OrderPaidMessage for orderId={}", message.orderId());

        Delivery delivery = new Delivery(
                message.orderId(),
                DeliveryStatus.CREATED,
                new DeliveryAddress("Baker street", "Chicago", "IL", "IL", "US"),
                LocalDate.now().plusDays(3),
                new TimeWindow(LocalTime.of(9, 0), LocalTime.of(18, 0)),
                UUID.randomUUID().toString()
        );
        Delivery saved = deliveryRepository.save(delivery);
        log.info("Created Delivery id={} for orderId={}", saved.getId(), message.orderId());

        var result = new DeliveryCreatedMessage(
                message.orderId(),
                saved.getId(),
                saved.getStatus().name()
        );
        kafkaTemplate.send(props.deliveryCreatedTopic(), message.orderId().toString(), result);
        log.info("Published DeliveryCreatedMessage for orderId={}", message.orderId());
    }
}