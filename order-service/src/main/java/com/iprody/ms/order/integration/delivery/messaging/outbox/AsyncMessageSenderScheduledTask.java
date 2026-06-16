package com.iprody.ms.order.integration.delivery.messaging.outbox;

import com.iprody.ms.order.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.order.service.outbox.AsyncMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncMessageSenderScheduledTask {
    private final AsyncMessageService asyncMessageService;
    private final AsyncMessageSenderProcessor processor;

    @Scheduled(fixedDelay = 3000)
    public void sendOutboxMessages() {
        List<AsyncMessage> messages = asyncMessageService.getUnsentOutboxMessages(50);
        if (!messages.isEmpty()) {
            log.info("Outbox: found {} unsent message(s), processing...", messages.size());
        }
        for (AsyncMessage message : messages) {
            processor.sendMessage(message);
        }
    }
}
