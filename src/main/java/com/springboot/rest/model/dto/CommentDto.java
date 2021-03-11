package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;

import java.time.LocalDate;
import java.util.Objects;

public class CommentDto implements ApiResourceMarker {

    Long id;

    UserDto owner;

    String commentContent;

    LocalDate commentedOnDate;

    LocalDate modifiedOnDate;

    Long noOfLikes;

    PostDto commentedOn;

    CommentDto parentComment;

    public CommentDto(Long id, UserDto owner, String commentContent, LocalDate commentedOnDate, LocalDate modifiedOnDate, Long noOfLikes, PostDto commentedOn, CommentDto parentComment) {
        this.id = id;
        this.owner = owner;
        this.commentContent = commentContent;
        this.commentedOnDate = commentedOnDate;
        this.modifiedOnDate = modifiedOnDate;
        this.noOfLikes = noOfLikes;
        this.commentedOn = commentedOn;
        this.parentComment = parentComment;
    }

    public CommentDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getCommentedOnDate() {
        return commentedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setCommentedOnDate(LocalDate commentedOnDate) {
        this.commentedOnDate = commentedOnDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getModifiedOnDate() {
        return modifiedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedOnDate(LocalDate modifiedOnDate) {
        this.modifiedOnDate = modifiedOnDate;
    }

    public Long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public PostDto getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(PostDto commentedOn) {
        this.commentedOn = commentedOn;
    }

    public CommentDto getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentDto parentComment) {
        this.parentComment = parentComment;
    }

    @Override
    public Long getOwnerId() {
        return this.owner.getId();
    }
}
