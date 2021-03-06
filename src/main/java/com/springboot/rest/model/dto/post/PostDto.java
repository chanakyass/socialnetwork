package com.springboot.rest.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.dto.ApiResourceMarker;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "Post",description = "Post structure. Contains the actual details of the post",  parent = PostProxyDto.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PostDto extends PostProxyDto implements ApiResourceMarker {

    String postHeading;

    String postBody;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfLikes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long noOfComments;

    Boolean isPostLikedByCurrentUser;

    LocalDateTime postedAtTime;

    LocalDateTime modifiedAtTime;

    public PostDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDateTime postedAtTime, LocalDateTime modifiedAtTime, Long noOfLikes, Long noOfComments, Boolean isPostLikedByCurrentUser) {
        super(id, owner);
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.postedAtTime = postedAtTime;
        this.modifiedAtTime = modifiedAtTime;
        this.noOfLikes = noOfLikes;
        this.noOfComments = noOfComments;
        this.isPostLikedByCurrentUser = isPostLikedByCurrentUser;
    }

    public PostDto() {
        super();
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getPostedAtTime() {
        return postedAtTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setPostedAtTime(LocalDateTime postedAtTime) {
        this.postedAtTime = postedAtTime;
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

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = Objects.requireNonNullElse(noOfLikes, 0L);
    }

    public Long getNoOfComments() {
        return Objects.requireNonNullElse(noOfComments, 0L);
    }

    public void setNoOfComments(Long noOfComments) {
        this.noOfComments = Objects.requireNonNullElse(noOfComments, 0L);
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }

    public Boolean getPostLikedByCurrentUser() {
        return isPostLikedByCurrentUser;
    }

    public void setPostLikedByCurrentUser(Boolean postLikedByCurrentUser) {
        isPostLikedByCurrentUser = postLikedByCurrentUser;
    }
}
