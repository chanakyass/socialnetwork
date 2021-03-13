package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.UserPersonalMarker;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class UserDto extends UserProxyDto implements UserPersonalMarker {

    String profileName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String password;
    Integer age;
    LocalDate DOB;
    String userSummary;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Role> grantedAuthoritiesList;

    public UserDto(Long id, String name, String profileName, String email, String password, Integer age, LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        super(id, name,email);
        this.profileName = profileName;
        this.password = password;
        this.age = age;
        this.DOB = DOB;
        this.userSummary = userSummary;
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

    public UserDto() {
        super();
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        Period between  = Period.between(getDOB(), LocalDate.now());
        this.age = between.getDays();
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(String userSummary) {
        this.userSummary = userSummary;
    }

    public List<Role> getGrantedAuthoritiesList() {
        return grantedAuthoritiesList;
    }

    public void setGrantedAuthoritiesList(List<Role> grantedAuthoritiesList) {
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }


}
