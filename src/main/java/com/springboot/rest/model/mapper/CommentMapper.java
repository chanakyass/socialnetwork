package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.service.LikeService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private LikeService likeService;

    public abstract Comment toComment(CommentDto commentDto);

    public abstract CommentDto toCommentDto(Comment comment);

    public abstract List<CommentDto> toCommentDtoList(List<Comment> comments);

    @AfterMapping
    public void afterToCommentDto(Comment request, @MappingTarget CommentDto commentDto)
    {
        commentDto.setCommentLikedByCurrentUser(likeService.hasLoggedInUserLikedComment(request.getId()));
    }

    @AfterMapping
    public void afterToCommentDtoList(List<Comment> comments, @MappingTarget List<CommentDto>commentDtoList){
        for(CommentDto commentDto: commentDtoList) {
            commentDto.setCommentLikedByCurrentUser(likeService.hasLoggedInUserLikedComment(commentDto.getId()));
        }
    }
}
