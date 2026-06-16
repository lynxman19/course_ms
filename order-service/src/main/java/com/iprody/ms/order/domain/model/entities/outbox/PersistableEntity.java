package com.iprody.ms.order.domain.model.entities.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Persistable;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

// Нужна, чтобы при создании и сохранении энтити с id, которое уже есть в базе,
// JPA не пыталась обновить существующую энтити через UPDATE, а пыталась выполнить INSERT
@MappedSuperclass
public abstract  class PersistableEntity<T> implements Persistable<T> {
    @Column(name = "created_at", insertable = false, updatable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
