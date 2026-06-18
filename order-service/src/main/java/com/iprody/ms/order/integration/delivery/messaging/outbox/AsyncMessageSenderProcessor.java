package com.iprody.ms.order.integration.delivery.messaging.outbox;

import com.iprody.ms.order.integration.delivery.messaging.dto.OrderPaidMessage;
import com.iprody.ms.order.service.outbox.AsyncMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.iprody.ms.order.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.order.common.SendingAsyncMessageException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncMessageSenderProcessor {
    private final AsyncMessageService asyncMessageService;
    private final KafkaTemplate<String, OrderPaidMessage> kafkaTemplate;
    private final JsonMapper mapper;

    /**
     * Отправляет асинхронное сообщение через Kafka и обновляет его статус.
     * Оборачивается в транзакцию для обеспечения атомарности.
     *
     * @param message сообщение, которое необходимо отправить
     */
    @Transactional
    public void sendMessage(AsyncMessage message) {
        try {
            OrderPaidMessage payload = mapper.readValue(message.getValue(), OrderPaidMessage.class);

            // Create the ProducerRecord
            ProducerRecord<String, OrderPaidMessage> producerRecord = new ProducerRecord<>(message.getTopic(),
                    message.getId().getId(),
                    payload);

            // Add custom transport headers if needed
            producerRecord.headers().add(new RecordHeader("X-Idempotency-Key", payload.orderId().toString().getBytes()));

            kafkaTemplate.send(producerRecord)
                    .exceptionally(e -> {
                        throw new SendingAsyncMessageException(
                                "Error sending outbox message id=%s".formatted(message.getId()), e);
                    })
                    .get();

            asyncMessageService.markAsSent(message);
            log.info("Outbox message sent and marked SENT: id={}, topic={}", message.getId().getId(), message.getTopic());
        } catch (Exception e) {
            throw new SendingAsyncMessageException(
                    "Error sending outbox message id=%s".formatted(message.getId()), e);
        }
    }
}
