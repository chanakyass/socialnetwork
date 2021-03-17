package com.springboot.rest.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel(value = "User edit", description = "Contains only the additional details of the user", parent = UserProxyDto.class)
public class UserEditDto extends UserProxyDto implements UserPersonalMarker {

    LocalDate DOB;

    String userSummary;

    public UserEditDto(Long id, String name, String email, LocalDate DOB, String userSummary) {
        super(id, name, email);
        this.DOB = DOB;
        this.userSummary = userSummary;
    }

    public UserEditDto() {
        super();
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
