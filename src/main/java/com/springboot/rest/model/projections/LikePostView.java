package com.springboot.rest.model.projections;

import java.time.LocalDate;

public interface LikePostView {

    Long getId();

    UserProxyView getOwner();

    LocalDate getLikedOnDate();
    PostProxyView getLikedPost();

}