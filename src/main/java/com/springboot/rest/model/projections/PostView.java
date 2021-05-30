package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Post;

public interface PostView {

    Post getPost();

    Long getNoOfLikes();

    Long getNoOfComments();

    Boolean getPostLikedByCurrentUser();

    void setPost(Post post);

     void setNoOfLikes(Long noOfLikes);

     void setNoOfComments(Long noOfComments);

     void setPostLikedByCurrentUser(Boolean postLikedByCurrentUser);

}
