package com.springboot.rest.model.dto.likes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.dto.ApiReactionMarker;
import com.springboot.rest.model.dto.ApiResourceMarker;
import com.springboot.rest.model.dto.post.PostProxyDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "LikePost", description = "Contains proxy for user and post")
public class LikePostDto implements ApiReactionMarker {

    Long id;

    UserProxyDto owner;

    LocalDateTime likedAtTime;

    PostProxyDto likedPost;

    public LikePostDto(Long id, UserProxyDto owner, LocalDateTime likedAtTime, PostProxyDto likedPost) {
        this.id = id;
        this.owner = owner;
        this.likedAtTime = likedAtTime;
        this.likedPost = likedPost;
    }

    public LikePostDto() {
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

    public PostProxyDto getLikedPost() {
        return likedPost;
    }

    public void setLikedPost(PostProxyDto likedPost) {
        this.likedPost = likedPost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikePostDto)) return false;
        LikePostDto that = (LikePostDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner()) && Objects.equals(getLikedAtTime(), that.getLikedAtTime()) && getLikedPost().equals(that.getLikedPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getLikedAtTime(), getLikedPost());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }

    @Override
    public Long getResourceIdForReaction() {
        return getLikedPost().getId();
    }
}
