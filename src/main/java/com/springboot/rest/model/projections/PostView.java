package com.springboot.rest.model.projections;

import java.time.LocalDate;

public interface PostView {

    Long getId();

    UserProxyView getOwner();

    String getPostHeading();

    String getPostBody();

    Long getNoOfLikes();

    LocalDate getPostedOnDate();

    LocalDate getModifiedOnDate();
}
