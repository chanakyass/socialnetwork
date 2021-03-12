package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;

import java.time.LocalDate;
import java.util.Objects;

public class CommentEditDto extends CommentProxyDto implements ApiResourceMarker {

    String commentContent;

    LocalDate modifiedOnDate;

    public CommentEditDto(Long id, UserProxyDto owner, String commentContent, LocalDate modifiedOnDate) {
        super(id, owner);
        this.commentContent = commentContent;
        this.modifiedOnDate = modifiedOnDate;
    }

    public CommentEditDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentEditDto)) return false;
        CommentEditDto that = (CommentEditDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
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

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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
