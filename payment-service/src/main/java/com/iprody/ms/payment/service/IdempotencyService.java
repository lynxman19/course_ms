package com.iprody.ms.payment.service;

import com.iprody.ms.payment.domain.model.entity.IdempotencyKey;
import com.iprody.ms.payment.domain.model.enums.IdempotencyKeyStatus;
import com.iprody.ms.payment.domain.repository.IdempotencyRepository;
import com.iprody.ms.payment.exception.IdempotencyKeyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final IdempotencyRepository repository;

    @Transactional
    public void createPendingKey(String key) {
        IdempotencyKey newKey = new IdempotencyKey(key, IdempotencyKeyStatus.PENDING);
        try {
            repository.save(newKey);
        } catch (DataIntegrityViolationException e) {
            throw new IdempotencyKeyExistsException("Key already exists", e);
        }
    }

    public Optional<IdempotencyKey> getByKey(String key) {
        return repository.findById(key);
    }

    public void markAsCompleted(String key, String responseData, int statusCode) {
        var keyEntity = getByKey(key).orElseThrow(() -> new EntityNotFoundException("Key not found"));
        keyEntity.setStatus(IdempotencyKeyStatus.COMPLETED);
        keyEntity.setStatusCode(statusCode);
        keyEntity.setResponse(responseData);
        repository.save(keyEntity);
    }
}
