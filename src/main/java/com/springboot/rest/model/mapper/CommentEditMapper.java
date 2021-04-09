package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.entities.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class CommentEditMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void update(CommentEditDto request, @MappingTarget Comment comment);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toCommentEditDto(Comment request, @MappingTarget CommentEditDto commentEditDto);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toCommentEditDto(CommentDto request, @MappingTarget CommentEditDto commentEditDto);
}
