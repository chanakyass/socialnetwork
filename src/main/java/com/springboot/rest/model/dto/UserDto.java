package com.springboot.rest.model.dto;

import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.UserPersonalMarker;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UserDto implements UserPersonalMarker {

    Long id;

    String name;

    String email;

    Integer age;

    LocalDate DOB;

    String userSummary;

    List<Role> grantedAuthoritiesList;

    public UserDto(Long id, String name, String email, Integer age, LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.DOB = DOB;
        this.userSummary = userSummary;
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

    public UserDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return getId().equals(userDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
