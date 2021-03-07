package com.springboot.rest.config.error;

import java.time.LocalDateTime;
import java.util.List;

public class ApiCallError<T> {
    private final String URI;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<T> details;

    public ApiCallError(String URI, String message, LocalDateTime timestamp, List<T> details) {
        this.URI = URI;
        this.message = message;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<T> getDetails() {
        return details;
    }

    public String getURI() {
        return URI;
    }
}
