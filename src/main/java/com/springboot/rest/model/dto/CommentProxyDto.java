package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Comment proxy", description = "Contains only the comment id and the owner proxy")
public class CommentProxyDto {

    Long id;
    UserProxyDto owner;

    public CommentProxyDto(Long id, UserProxyDto owner) {
        this.id = id;
        this.owner = owner;
    }

    public CommentProxyDto() {
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
        if (!(o instanceof CommentProxyDto)) return false;
        CommentProxyDto that = (CommentProxyDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
    }
}
