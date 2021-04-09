package com.springboot.rest.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.dto.ApiResourceMarker;
import com.springboot.rest.model.dto.post.PostProxyDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "Comment", description = "Contains a proxy for the post, parent comment", parent = CommentProxyDto.class)
public class CommentDto extends CommentProxyDto implements ApiResourceMarker {

    LocalDateTime commentedAtTime;
    LocalDateTime modifiedAtTime;
    PostProxyDto commentedOn;
    CommentProxyDto parentComment;
    String commentContent;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfLikes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfReplies;

    Boolean isCommentLikedByCurrentUser;

    public CommentDto(Long id, UserProxyDto owner, LocalDateTime commentedAtTime, LocalDateTime modifiedAtTime, PostProxyDto commentedOn, CommentProxyDto parentComment, String commentContent, Long noOfLikes, Long noOfReplies, Boolean isCommentLikedByCurrentUser) {
        super(id, owner);
        this.commentedAtTime = commentedAtTime;
        this.modifiedAtTime = modifiedAtTime;
        this.commentedOn = commentedOn;
        this.parentComment = parentComment;
        this.commentContent = commentContent;
        this.noOfLikes = noOfLikes;
        this.noOfReplies = noOfReplies;
        this.isCommentLikedByCurrentUser = isCommentLikedByCurrentUser;
    }

    public CommentDto()
    {
        super();
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getCommentedAtTime() {
        return commentedAtTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setCommentedAtTime(LocalDateTime commentedAtTime) {
        this.commentedAtTime = commentedAtTime;
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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getModifiedAtTime() {
        return modifiedAtTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setModifiedAtTime(LocalDateTime modifiedAtTime) {
        this.modifiedAtTime = modifiedAtTime;
    }

    public Long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = Objects.requireNonNullElse(noOfLikes, 0L);
    }

    public Long getNoOfReplies() {
        return Objects.requireNonNullElse(noOfReplies, 0L);
    }

    public void setNoOfReplies(Long noOfReplies) {
        this.noOfReplies = Objects.requireNonNullElse(noOfReplies, 0L);
    }

    public Boolean getCommentLikedByCurrentUser() {
        return isCommentLikedByCurrentUser;
    }

    public void setCommentLikedByCurrentUser(Boolean commentLikedByCurrentUser) {
        isCommentLikedByCurrentUser = commentLikedByCurrentUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner()) && Objects.equals(getCommentedAtTime(), that.getCommentedAtTime()) && getCommentedOn().equals(that.getCommentedOn()) && Objects.equals(getParentComment(), that.getParentComment()) && Objects.equals(getCommentContent(), that.getCommentContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getCommentedAtTime(), getCommentedOn(), getParentComment(), getCommentContent());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }

}
