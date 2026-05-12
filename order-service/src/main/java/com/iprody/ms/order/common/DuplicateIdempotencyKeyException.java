package com.iprody.ms.order.common;

public class DuplicateIdempotencyKeyException extends RuntimeException{
    public DuplicateIdempotencyKeyException(String message) {
        super(message);
    }
}
