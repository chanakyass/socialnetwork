package com.springboot.rest.config.exceptions;

public class ApiAccessException extends RuntimeException {

    public ApiAccessException(String message) {
        super(message);
    }

    public ApiAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
