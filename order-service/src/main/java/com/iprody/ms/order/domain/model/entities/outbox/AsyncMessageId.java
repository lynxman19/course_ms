package com.iprody.ms.order.domain.model.entities.outbox;

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
