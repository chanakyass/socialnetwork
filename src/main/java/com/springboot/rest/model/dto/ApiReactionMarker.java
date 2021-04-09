package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ApiReactionMarker {

    @JsonIgnore
    Long getResourceIdForReaction();
    @JsonIgnore
    Long getOwnerId();
}
