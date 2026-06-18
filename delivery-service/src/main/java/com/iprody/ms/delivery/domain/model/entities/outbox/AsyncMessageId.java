package com.iprody.ms.delivery.domain.model.entities.outbox;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AsyncMessageId {
    private String id;
    private String topic;
}
