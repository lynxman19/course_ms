package com.iprody.ms.delivery.integration.order.messaging.listener;

import com.iprody.ms.delivery.integration.order.messaging.dto.OrderPaidMessage;
import com.iprody.ms.delivery.service.outbox.AsyncMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
public class OrderPaidListener extends IdempotentKafkaListener<OrderPaidMessage> {
    public OrderPaidListener(AsyncMessageService messageService, JsonMapper mapper) {
        super(mapper, messageService);
    }

    @KafkaListener(topics = "${kafka.order.order-paid-topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(OrderPaidMessage message,
                        ConsumerRecord<String, OrderPaidMessage> consumerRecord,
                        Acknowledgment ack) {
        super.consume(message, consumerRecord, ack);
    }

    @Override
    @Transactional
    public void processConsumedMessage(OrderPaidMessage message) {
        log.info("Consumed insurance confirmation created response message: " + message);
    }
}