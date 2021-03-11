package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void update(CommentDto request, @MappingTarget Comment comment);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toCommentDto(Comment request, @MappingTarget CommentDto postDto);
}
