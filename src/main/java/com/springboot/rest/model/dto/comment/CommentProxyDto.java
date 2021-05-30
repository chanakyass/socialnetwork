package com.springboot.rest.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Comment proxy", description = "Contains only the comment id and the owner proxy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentProxyDto {

    Long id;
    UserProxyDto owner;
}
