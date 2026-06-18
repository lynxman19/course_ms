package com.iprody.ms.delivery.domain.repository;

import com.iprody.ms.delivery.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.delivery.domain.model.entities.outbox.AsyncMessageId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsyncMessageRepository extends JpaRepository<AsyncMessage, AsyncMessageId> {
    @Query("SELECT m FROM AsyncMessage m WHERE m.status = 'CREATED' AND m.type = 'OUTBOX' ORDER BY m.createdAt")
    List<AsyncMessage> findUnsentOutboxMessages(Pageable pageable);
}
