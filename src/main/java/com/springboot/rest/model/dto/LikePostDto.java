package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;

import java.time.LocalDate;
import java.util.Objects;

public class LikePostDto implements ApiResourceMarker {

    Long id;

    UserProxyDto owner;

    LocalDate likedOnDate;

    PostProxyDto likedPost;

    public LikePostDto(Long id, UserProxyDto owner, LocalDate likedOnDate, PostProxyDto likedPost) {
        this.id = id;
        this.owner = owner;
        this.likedOnDate = likedOnDate;
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

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getLikedOnDate() {
        return likedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setLikedOnDate(LocalDate likedOnDate) {
        this.likedOnDate = likedOnDate;
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
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner()) && Objects.equals(getLikedOnDate(), that.getLikedOnDate()) && getLikedPost().equals(that.getLikedPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getLikedOnDate(), getLikedPost());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
