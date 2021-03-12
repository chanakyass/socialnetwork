package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;

import java.time.LocalDate;
import java.util.Objects;

public class PostEditDto implements ApiResourceMarker {

    Long id;

    UserProxyDto owner;

    String postHeading;

    String postBody;

    LocalDate modifiedOnDate;

    public PostEditDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDate modifiedOnDate) {
        this.id = id;
        this.owner = owner;
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.modifiedOnDate = modifiedOnDate;
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
    public LocalDate getModifiedOnDate() {
        return modifiedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedOnDate(LocalDate modifiedOnDate) {
        this.modifiedOnDate = modifiedOnDate;
    }

    @Override
    public Long getOwnerId() {
        return this.owner.getId();
    }
}
