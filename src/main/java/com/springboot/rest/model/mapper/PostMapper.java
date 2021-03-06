package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostProxyDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.PostView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    public abstract Post toPost(PostDto postDto);

    @Mapping(target = "postLikedByCurrentUser", ignore = true)
    @Mapping(target = "noOfLikes", ignore = true)
    @Mapping(target = "noOfComments", ignore = true)
    public abstract PostDto toPostDto(Post request);

    @Mapping(target = "postLikedByCurrentUser", ignore = true)
    @Mapping(target = "noOfLikes", ignore = true)
    @Mapping(target = "noOfComments", ignore = true)

    public abstract List<PostDto> toPostDtoList(List<Post> posts);

    public PostDto toPostDtoFromView(PostView request){
        if(request != null){
            PostDto postDto = toPostDto(request.getPost());
            postDto.setNoOfLikes(request.getNoOfLikes());
            postDto.setNoOfComments(request.getNoOfComments());
            postDto.setPostLikedByCurrentUser(request.getPostLikedByCurrentUser());
            return postDto;
        }
        return null;
    }

    public abstract PostProxyDto toPostProxyDto(PostDto postDto);

    public abstract List<PostDto> toPostDtoListFromView(List<PostView> posts);

}
