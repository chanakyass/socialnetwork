package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import java.time.LocalDateTime;

public interface PostView {
//    Long getId();
//
//    User getOwner();
//
//    String getPostHeading();
//
//    String getPostBody();
//
//    LocalDateTime getPostedAtTime();
//
//    LocalDateTime getModifiedAtTime();

    Post getPost();

    Long getNoOfLikes();

    Long getNoOfComments();
}
