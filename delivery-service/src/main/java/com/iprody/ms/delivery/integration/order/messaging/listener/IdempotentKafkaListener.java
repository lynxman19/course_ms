package com.iprody.ms.delivery.integration.order.messaging.listener;

import com.iprody.ms.delivery.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.delivery.domain.model.valueobjects.AsyncMessageStatus;
import com.iprody.ms.delivery.domain.model.valueobjects.AsyncMessageType;
import com.iprody.ms.delivery.service.outbox.AsyncMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.support.Acknowledgment;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public abstract class IdempotentKafkaListener<T> {
    private final JsonMapper mapper;
    private final AsyncMessageService messageService;

    /**
     *
     * Обработка полученного Kafka сообщения с проверкой идемпотентности.
     * @param consumerRecord исходный Kafka рекорд
     * @param message расшифрованное сообщение
     * @param ack подтверждение обработки
     * @throws JsonProcessingException при ошибке сериализации сообщения
     */
    public void consume(T message,
                        ConsumerRecord<String, T> consumerRecord,
                        Acknowledgment ack) {
        // Получение заголовка с ключом идемпотентности
        Header idempotentKeyHeader =  consumerRecord.headers().lastHeader("X-Idempotency-Key");

        if (idempotentKeyHeader == null) {
            log.error("Idempotent key header is null for consumer record " + consumerRecord);
            ack.acknowledge();
            return;
        }

        // Преобразование заголовка в строку
        String idempotentKey = new String(idempotentKeyHeader.value(), StandardCharsets.UTF_8);

        // Создание объекта для хранения в базе данных
        AsyncMessage asyncMessage = AsyncMessage.builder()
                .id(idempotentKey)
                .topic(consumerRecord.topic())
                .value(mapper.writeValueAsString(message))
                .status(AsyncMessageStatus.RECEIVED)
                .type(AsyncMessageType.INBOX)
                .build();

        try {
            // Сохранение сообщения для проверки идемпотентности
            messageService.saveMessage(asyncMessage);
        } catch (DataIntegrityViolationException e) {
            // Если сообщение с таким ключом уже есть -- логируем и подтверждаем
            log.warn("Message with the same idempotent key is present in DB: " + idempotentKey);
            ack.acknowledge();
            return;
        }

        // Обработка сообщения
        processConsumedMessage(message);
        ack.acknowledge();
    }

    /**
     * Метод для обработки конкретных сообщений, реализуемый в
     * @param message полученное сообщение
     */
    public abstract void processConsumedMessage(T message);
}
