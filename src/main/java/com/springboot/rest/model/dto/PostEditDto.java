package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostEditDto implements ApiResourceMarker {

    Long id;

    UserProxyDto owner;

    String postHeading;

    String postBody;

    LocalDateTime modifiedAtTime;

    public PostEditDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDateTime modifiedAtTime) {
        this.id = id;
        this.owner = owner;
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.modifiedAtTime = modifiedAtTime;
    }

    public PostEditDto() {
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
        if (!(o instanceof PostEditDto)) return false;
        PostEditDto postEditDto = (PostEditDto) o;
        return getId().equals(postEditDto.getId()) && getOwner().equals(postEditDto.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
    }



    public String getPostHeading() {
        return postHeading;
    }

    public void setPostHeading(String postHeading) {
        this.postHeading = postHeading;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDateTime getModifiedAtTime() {
        return modifiedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedAtTime(LocalDateTime modifiedAtTime) {
        this.modifiedAtTime = modifiedAtTime;
    }

    @Override
    public Long getOwnerId() {
        return this.owner.getId();
    }
}
