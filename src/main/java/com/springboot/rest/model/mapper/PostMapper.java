package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.service.LikeService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private LikeService likeService;


    public abstract Post toPost(PostDto postDto);

    @Mapping(target = "postLikedByCurrentUser", ignore = true)
    public abstract PostDto toPostDto(Post request);

    public abstract List<PostDto> toPostDtoList(List<Post> posts);

    @AfterMapping
    public void afterToPostDto(Post request, @MappingTarget PostDto postDto)
    {
        postDto.setPostLikedByCurrentUser(likeService.hasLoggedInUserLikedPost(request.getId()));
    }

    @AfterMapping
    public void afterToPostDtoList(List<Post> posts, @MappingTarget List<PostDto>postDtoList){
        for(PostDto postDto: postDtoList) {
            postDto.setPostLikedByCurrentUser(likeService.hasLoggedInUserLikedPost(postDto.getId()));
        }
    }

}
