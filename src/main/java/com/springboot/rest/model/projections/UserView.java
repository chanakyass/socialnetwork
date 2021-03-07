package com.springboot.rest.model.projections;

import com.springboot.rest.model.entities.Role;

import java.time.LocalDate;
import java.util.List;

public interface UserView {

    Long getId();

    String getName();

    String getProfileName();

    LocalDate getDOB();

    String getUserSummary();

    List<Role> getGrantedAuthoritiesList();

}