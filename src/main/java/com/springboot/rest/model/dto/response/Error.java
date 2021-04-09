package com.springboot.rest.model.dto.response;

public class Error<T> {

    T error;

    public Error(T error) {
        this.error = error;
    }


    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
