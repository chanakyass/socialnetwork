package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(description = "Post details", parent = PostProxyDto.class)
public class PostDto extends PostProxyDto implements ApiResourceMarker {

    String postHeading;

    String postBody;

    LocalDateTime postedAtTime;

    LocalDateTime modifiedAtTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfLikes;

    public PostDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDateTime postedAtTime, LocalDateTime modifiedAtTime, Long noOfLikes) {
        super(id, owner);
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.postedAtTime = postedAtTime;
        this.modifiedAtTime = modifiedAtTime;
        this.noOfLikes = noOfLikes;
    }

    public PostDto() {
        super();
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
    public LocalDateTime getPostedAtTime() {
        return postedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setPostedAtTime(LocalDateTime postedAtTime) {
        this.postedAtTime = postedAtTime;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDateTime getModifiedAtTime() {
        return modifiedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedAtTime(LocalDateTime modifiedAtTime) {
        this.modifiedAtTime = modifiedAtTime;
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
        if (!(o instanceof PostDto)) return false;
        PostDto postDto = (PostDto) o;
        return getId().equals(postDto.getId()) && getOwner().equals(postDto.getOwner()) && Objects.equals(getPostHeading(), postDto.getPostHeading()) && Objects.equals(getPostBody(), postDto.getPostBody()) && Objects.equals(getPostedAtTime(), postDto.getPostedAtTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getPostHeading(), getPostBody(), getPostedAtTime());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
