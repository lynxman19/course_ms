package com.iprody.ms.order.service.outbox;

import com.iprody.ms.order.domain.model.entities.outbox.AsyncMessage;
import java.util.List;

public interface AsyncMessageService {
    void saveMessage(AsyncMessage message);

    List<AsyncMessage> getUnsentOutboxMessages(int batchSize);

    void markAsSent(AsyncMessage message);
}
