package com.iprody.ms.delivery.service.outbox;


import com.iprody.ms.delivery.domain.model.entities.outbox.AsyncMessage;

import java.util.List;

public interface AsyncMessageService {
    void saveMessage(AsyncMessage message);

    List<AsyncMessage> getUnsentOutboxMessages(int batchSize);

    void markAsSent(AsyncMessage message);
}
