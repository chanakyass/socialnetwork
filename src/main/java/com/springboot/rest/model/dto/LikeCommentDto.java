package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "LikeComment", description = "Contains proxy for user and comment")
public class LikeCommentDto implements ApiResourceMarker {

    Long id;

    UserProxyDto owner;

    LocalDateTime likedAtTime;

    CommentProxyDto likedComment;

    public LikeCommentDto(Long id, UserProxyDto owner, LocalDateTime likedAtTime, CommentProxyDto likedComment) {
        this.id = id;
        this.owner = owner;
        this.likedAtTime = likedAtTime;
        this.likedComment = likedComment;
    }

    public LikeCommentDto() {
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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getLikedAtTime() {
        return likedAtTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setLikedAtTime(LocalDateTime likedAtTime) {
        this.likedAtTime = likedAtTime;
    }

    public CommentProxyDto getLikedComment() {
        return likedComment;
    }

    public void setLikedComment(CommentProxyDto likedComment) {
        this.likedComment = likedComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeCommentDto)) return false;
        LikeCommentDto that = (LikeCommentDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner()) && Objects.equals(getLikedAtTime(), that.getLikedAtTime()) && getLikedComment().equals(that.getLikedComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getLikedAtTime(), getLikedComment());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
