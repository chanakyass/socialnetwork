package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

public class UserEditDto implements UserPersonalMarker {

    Long id;

    String name;

    String email;

    LocalDate DOB;

    String userSummary;

    public UserEditDto(Long id, String name, String email, LocalDate DOB, String userSummary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.userSummary = userSummary;
    }

    public UserEditDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEditDto)) return false;
        UserEditDto userEditDto = (UserEditDto) o;
        return getId().equals(userEditDto.getId());
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

}
