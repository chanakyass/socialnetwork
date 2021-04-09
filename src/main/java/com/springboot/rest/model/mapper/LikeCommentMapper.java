package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.entities.LikeComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LikeCommentMapper {

    public abstract LikeComment toLikeComment(LikeCommentDto likeCommentDto);

    public abstract LikeCommentDto toLikeCommentDto(LikeComment likeComment);

    public abstract List<LikeCommentDto> toLikeCommentDtoList(List<LikeComment> likes);

}
