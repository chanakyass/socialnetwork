package com.springboot.rest.model.dto;

import com.springboot.rest.model.entities.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UserEdit {

    Long id;

    String name;

    String email;

    Long age;

    LocalDate DOB;

    String userSummary;

    List<Role> grantedAuthoritiesList;

    public UserEdit(Long id, String name, String email, Long age, LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.DOB = DOB;
        this.userSummary = userSummary;
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

    public UserEdit() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEdit)) return false;
        UserEdit userEdit = (UserEdit) o;
        return getId().equals(userEdit.getId());
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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
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
