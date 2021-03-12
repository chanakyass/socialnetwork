package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;

import java.time.LocalDate;
import java.util.Objects;

public class CommentDto extends CommentProxyDto implements ApiResourceMarker {

    LocalDate commentedOnDate;
    LocalDate modifiedOnDate;
    PostProxyDto commentedOn;
    CommentProxyDto parentComment;
    String commentContent;
    Long noOfLikes;

    public CommentDto(Long id, UserProxyDto owner, LocalDate commentedOnDate, LocalDate modifiedOnDate, PostProxyDto commentedOn, CommentProxyDto parentComment, String commentContent, Long noOfLikes) {
        super(id, owner);
        this.commentedOnDate = commentedOnDate;
        this.modifiedOnDate = modifiedOnDate;
        this.commentedOn = commentedOn;
        this.parentComment = parentComment;
        this.commentContent = commentContent;
        this.noOfLikes = noOfLikes;
    }

    public CommentDto()
    {
        super();
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getCommentedOnDate() {
        return commentedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setCommentedOnDate(LocalDate commentedOnDate) {
        this.commentedOnDate = commentedOnDate;
    }

    public PostProxyDto getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(PostProxyDto commentedOn) {
        this.commentedOn = commentedOn;
    }

    public CommentProxyDto getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentProxyDto parentComment) {
        this.parentComment = parentComment;
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

    public Long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = Objects.requireNonNullElse(noOfLikes, 0L);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner()) && Objects.equals(getCommentedOnDate(), that.getCommentedOnDate()) && getCommentedOn().equals(that.getCommentedOn()) && Objects.equals(getParentComment(), that.getParentComment()) && Objects.equals(getCommentContent(), that.getCommentContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getCommentedOnDate(), getCommentedOn(), getParentComment(), getCommentContent());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
