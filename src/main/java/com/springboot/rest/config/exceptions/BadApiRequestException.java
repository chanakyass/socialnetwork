package com.springboot.rest.config.exceptions;

public class BadApiRequestException extends RuntimeException{

    public BadApiRequestException()
    {
        super("Bad request");
    }

    public BadApiRequestException(String message) {
        super(message);
    }

    public BadApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
