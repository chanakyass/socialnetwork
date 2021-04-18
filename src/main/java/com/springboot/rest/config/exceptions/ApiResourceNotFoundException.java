package com.springboot.rest.config.exceptions;

public class ApiResourceNotFoundException extends RuntimeException{
    public ApiResourceNotFoundException(){
        super("Resource not found");
    }

    public ApiResourceNotFoundException(String message) {
        super(message);
    }

    public ApiResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
