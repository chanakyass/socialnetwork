package com.springboot.rest.config.exceptions;

public class ApiSpecificException extends RuntimeException {

    public ApiSpecificException(String message) {
        super(message);
    }

    public ApiSpecificException(String message, Throwable cause) {
        super(message, cause);
    }
}
