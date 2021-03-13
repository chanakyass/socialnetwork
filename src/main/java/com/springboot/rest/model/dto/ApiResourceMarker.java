package com.springboot.rest.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ApiResourceMarker {

    Long getId();

    @JsonIgnore
    Long getOwnerId();

}
