package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Comment;

public interface CommentView {
    Comment getComment();
    Long getNoOfLikes();
    Long getNoOfReplies();
    Boolean getCommentLikedByCurrentUser();
}
