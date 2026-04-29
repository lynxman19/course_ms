package com.iprody.ms.payment.exception;

public abstract class BaseServiceException extends RuntimeException{
    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
