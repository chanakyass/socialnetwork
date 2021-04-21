package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.PostView;
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

    public abstract List<PostDto> toPostDtoListFromView(List<PostView> posts);

//    @AfterMapping
//    public void afterToPostDto(Post request, @MappingTarget PostDto postDto)
//    {
//        postDto.setPostLikedByCurrentUser(likeService.hasLoggedInUserLikedPost(request.getId()));
//    }
//
//    @AfterMapping
//    public void afterToPostDtoList(List<Post> posts, @MappingTarget List<PostDto>postDtoList){
//        for(PostDto postDto: postDtoList) {
//            postDto.setPostLikedByCurrentUser(likeService.hasLoggedInUserLikedPost(postDto.getId()));
//        }
//    }

}
