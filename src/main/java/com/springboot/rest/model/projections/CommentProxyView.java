package com.springboot.rest.model.projections;

public interface CommentProxyView {

    Long getId();

    UserProxyView getOwner();
}
