package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Post;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    public abstract Post toPost(PostDto postDto);

    public abstract PostDto toPostDto(Post request);

    public abstract List<PostDto> toPostDtoList(List<Post> posts);
}
