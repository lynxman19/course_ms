package com.iprody.ms.order.service.outbox;

import com.iprody.ms.order.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.order.domain.model.valueobjects.AsyncMessageStatus;
import com.iprody.ms.order.domain.repository.AsyncMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncMessageServiceImpl implements AsyncMessageService {
    private final AsyncMessageRepository repository;

    @Override
    @Transactional
    public void saveMessage(AsyncMessage message) {
        repository.save(message);
    }

    @Override
    public List<AsyncMessage> getUnsentOutboxMessages(int batchSize) {
        return repository.findUnsentOutboxMessages(Pageable.ofSize(batchSize));
    }

    @Override
    @Transactional
    public void markAsSent(AsyncMessage message) {
        message.setStatus(AsyncMessageStatus.SENT);
        repository.save(message);
    }
}
