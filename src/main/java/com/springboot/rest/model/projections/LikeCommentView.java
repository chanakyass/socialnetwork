package com.springboot.rest.model.projections;

import java.time.LocalDate;

public interface LikeCommentView {

    Long getId();

    UserProxyView getOwner();

    LocalDate getLikedOnDate();

    CommentProxyView getLikedComment();

}