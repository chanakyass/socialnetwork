package com.springboot.rest.model.projections;

import java.time.LocalDate;

public interface CommentView {

     Long getId();

     UserProxyView getOwner();

     String getCommentContent();

     LocalDate getCommentedOnDate();

     LocalDate getModifiedOnDate();

     Long getNoOfLikes();

     PostProxyView getCommentedOn();

     CommentProxyView getParentComment();

}