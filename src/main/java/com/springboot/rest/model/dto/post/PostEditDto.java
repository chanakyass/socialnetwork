package com.springboot.rest.model.dto.post;

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

@ApiModel(value = "Post Edit",  description = "Contains the actual details of the post. Only used for editing a post",  parent = PostProxyDto.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PostEditDto extends PostProxyDto implements ApiResourceMarker {

    String postHeading;

    String postBody;

    LocalDateTime modifiedAtTime;

    public PostEditDto(Long id, UserProxyDto owner, String postHeading, String postBody, LocalDateTime modifiedAtTime) {
        super(id, owner);
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.modifiedAtTime = modifiedAtTime;
    }

    public PostEditDto() {
        super();
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
