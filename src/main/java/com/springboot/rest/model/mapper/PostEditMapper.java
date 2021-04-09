package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.entities.Post;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class PostEditMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toPost(PostEditDto request, @MappingTarget Post post);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toPostEditDto(Post request, @MappingTarget PostEditDto postEditDto);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toPostEditDto(PostDto request, @MappingTarget PostEditDto postEditDto);


}
