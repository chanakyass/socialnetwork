package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Comment;

public interface CommentView {
    Comment getComment();
    Long getNoOfLikes();
    Long getNoOfReplies();
    Boolean getCommentLikedByCurrentUser();
    void setComment(Comment comment);
    void setNoOfLikes(Long noOfLikes);
    void setNoOfReplies(Long noOfReplies);
    void setCommentLikedByCurrentUser(Boolean commentLikedByCurrentUser);
}
