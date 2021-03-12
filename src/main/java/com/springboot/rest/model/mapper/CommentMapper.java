package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.entities.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    public abstract Comment toComment(CommentDto commentDto);

    public abstract CommentDto toCommentDto(Comment comment);

    public abstract List<CommentDto> toCommentDtoList(List<Comment> comments);
}
