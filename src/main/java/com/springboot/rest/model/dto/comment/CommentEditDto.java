package com.springboot.rest.model.dto.comment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.dto.ApiResourceMarker;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel(value = "Comment Edit", description = "This object is used only for edit operation.", parent = CommentProxyDto.class)
public class CommentEditDto extends CommentProxyDto implements ApiResourceMarker {

    String commentContent;

    LocalDateTime modifiedAtTime;

    public CommentEditDto(Long id, UserProxyDto owner, String commentContent, LocalDateTime modifiedAtTime) {
        super(id, owner);
        this.commentContent = commentContent;
        this.modifiedAtTime = modifiedAtTime;
    }

    public CommentEditDto() {
        super();
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

    @Override
    public Long getOwnerId() {
        return this.owner.getId();
    }
}
