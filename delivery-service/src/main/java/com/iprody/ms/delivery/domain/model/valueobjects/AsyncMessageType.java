package com.iprody.ms.delivery.domain.model.valueobjects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AsyncMessageType {
    INBOX("INBOX"), OUTBOX("OUTBOX");

    private final String code;
}
