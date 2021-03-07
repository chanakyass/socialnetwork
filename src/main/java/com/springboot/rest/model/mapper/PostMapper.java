package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Post;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void update(PostDto request, @MappingTarget Post post);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toPostEdit(Post request, @MappingTarget PostDto postDto);
}
