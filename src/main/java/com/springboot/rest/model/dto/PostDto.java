package com.springboot.rest.model.dto;

import com.springboot.rest.model.entities.ApiResourceMarker;
import com.springboot.rest.model.entities.User;

import java.time.LocalDate;
import java.util.Objects;

public class PostDto implements ApiResourceMarker {

    Long id;

    UserDto owner;

    String postHeading;

    String postBody;

    Long noOfLikes;

    LocalDate postedOnDate;

    LocalDate modifiedOnDate;

    public PostDto(Long id, UserDto owner, String postHeading, String postBody, Long noOfLikes, LocalDate postedOnDate, LocalDate modifiedOnDate) {
        this.id = id;
        this.owner = owner;
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.noOfLikes = noOfLikes;
        this.postedOnDate = postedOnDate;
        this.modifiedOnDate = modifiedOnDate;
    }

    public PostDto() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto)) return false;
        PostDto postDto = (PostDto) o;
        return getId().equals(postDto.getId()) && getOwner().equals(postDto.getOwner());
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

    public Long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public LocalDate getPostedOnDate() {
        return postedOnDate;
    }

    public void setPostedOnDate(LocalDate postedOnDate) {
        this.postedOnDate = postedOnDate;
    }

    public LocalDate getModifiedOnDate() {
        return modifiedOnDate;
    }

    public void setModifiedOnDate(LocalDate modifiedOnDate) {
        this.modifiedOnDate = modifiedOnDate;
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
