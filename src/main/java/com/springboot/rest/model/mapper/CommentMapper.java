package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.projections.CommentView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    public abstract Comment toComment(CommentDto commentDto);

    public abstract CommentDto toCommentDto(Comment comment);

    public abstract List<CommentDto> toCommentDtoList(List<Comment> comments);

    public CommentDto toCommentDtoFromView(CommentView request){
        if(request != null){
            CommentDto commentDto = toCommentDto(request.getComment());
            commentDto.setNoOfLikes(request.getNoOfLikes());
            commentDto.setNoOfReplies(request.getNoOfReplies());
            commentDto.setCommentLikedByCurrentUser(request.getCommentLikedByCurrentUser());
            return commentDto;
        }
        return null;
    }

    public abstract List<CommentDto> toCommentDtoListFromView(List<CommentView> comments);

}
