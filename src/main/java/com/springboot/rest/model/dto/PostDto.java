package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.entities.ApiResourceMarker;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;
import java.util.Objects;

@ApiModel(description = "Post details", parent = PostProxyDto.class)
public class PostDto extends PostProxyDto implements ApiResourceMarker {

    String postHeading;

    String postBody;

    LocalDate postedOnDate;

    LocalDate modifiedOnDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfLikes;

    public PostDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDate postedOnDate, LocalDate modifiedOnDate, Long noOfLikes) {
        super(id, owner);
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.postedOnDate = postedOnDate;
        this.modifiedOnDate = modifiedOnDate;
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
    public LocalDate getPostedOnDate() {
        return postedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setPostedOnDate(LocalDate postedOnDate) {
        this.postedOnDate = postedOnDate;
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
        if (!(o instanceof PostDto)) return false;
        PostDto postDto = (PostDto) o;
        return getId().equals(postDto.getId()) && getOwner().equals(postDto.getOwner()) && Objects.equals(getPostHeading(), postDto.getPostHeading()) && Objects.equals(getPostBody(), postDto.getPostBody()) && Objects.equals(getPostedOnDate(), postDto.getPostedOnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getPostHeading(), getPostBody(), getPostedOnDate());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
