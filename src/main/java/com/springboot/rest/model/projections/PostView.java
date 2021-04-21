package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Post;

public interface PostView {

    Post getPost();

    Long getNoOfLikes();

    Long getNoOfComments();

    Boolean getPostLikedByCurrentUser();
}
