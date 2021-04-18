package com.springboot.rest.config.exceptions;

public enum ExceptionType {

    API_SPECIFIC_EXCEPTION("API_SPECIFIC_EXCEPTION"),
    API_RESOURCE_NOT_FOUND_EXCEPTION("API_RESOURCE_NOT_FOUND_EXCEPTION"),
    OTHER_EXCEPTION("OTHER_EXCEPTION");

    private final String type;

    ExceptionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
