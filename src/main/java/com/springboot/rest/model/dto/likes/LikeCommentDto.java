package com.springboot.rest.model.dto.likes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.dto.ApiReactionMarker;
import com.springboot.rest.model.dto.comment.CommentProxyDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel(value = "LikeComment", description = "Contains proxy for user and comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCommentDto implements ApiReactionMarker {

    Long id;

    UserProxyDto owner;

    LocalDateTime likedAtTime;

    CommentProxyDto likedComment;

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

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }

    @Override
    public Long getResourceIdForReaction() {
        return getLikedComment().getId();
    }
}
