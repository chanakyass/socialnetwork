package com.springboot.rest.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ApiResourceMarker {

    Long getId();

    @JsonIgnore
    Long getOwnerId();

}
