package com.springboot.rest.model.dto;

public class Data<T> {
    T data;

    public Data(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
