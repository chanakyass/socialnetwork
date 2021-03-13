package com.springboot.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.rest.model.entities.Role;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class UserDto extends UserProxyDto implements UserPersonalMarker {

    String profileName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer age;

    LocalDate DOB;
    String userSummary;
    List<Role> grantedAuthoritiesList;

    public UserDto(Long id, String name, String profileName, String email, String password, LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        super(id, name,email);
        this.profileName = profileName;
        this.password = password;
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
        Period period;
        if(getDOB() != null) {
            period = Period.between(getDOB(), LocalDate.now());
            return period.getYears();
        }
        return null;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getDOB() {
        return DOB;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
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
