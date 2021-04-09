package com.springboot.rest.config.error;

import java.time.LocalDateTime;
import java.util.List;

public class ApiCallError {
    private final String URI;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> details;

    public ApiCallError(String URI, String message, LocalDateTime timestamp, List<String> details) {
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

    public List<String> getDetails() {
        return details;
    }

    public String getURI() {
        return URI;
    }
}
