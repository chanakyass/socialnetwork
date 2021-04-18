package com.springboot.rest.config.error;

import com.springboot.rest.config.exceptions.ExceptionType;

import java.time.LocalDateTime;
import java.util.List;

public class ApiCallError {
    private int statusCode;
    private final String URI;
    private ExceptionType exceptionType;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public ApiCallError(int statusCode, String URI, ExceptionType exceptionType, String message, LocalDateTime timestamp, List<String> details) {
        this.statusCode = statusCode;
        this.URI = URI;
        this.exceptionType = exceptionType;
        this.message = message;
        this.timestamp = timestamp;
        this.details = details;
    }

    public ApiCallError(int statusCode, String URI,  String message, LocalDateTime timestamp, List<String> details) {
        this.statusCode = statusCode;
        this.URI = URI;
        this.exceptionType = ExceptionType.OTHER_EXCEPTION;
        this.message = message;
        this.timestamp = timestamp;
        this.details = details;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }

    public String getURI() {
        return URI;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
