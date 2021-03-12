package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostProxyDto {

    Long id;
    UserProxyDto owner;

    public PostProxyDto(Long id, UserProxyDto owner) {
        this.id = id;
        this.owner = owner;
    }

    public PostProxyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProxyDto getOwner() {
        return owner;
    }

    public void setOwner(UserProxyDto owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostProxyDto)) return false;
        PostProxyDto that = (PostProxyDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
    }
}
