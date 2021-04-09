package com.springboot.rest.model.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.dto.UserPersonalMarker;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel(value = "User edit", description = "Contains only the additional details of the user", parent = UserProxyDto.class)
public class UserEditDto extends UserProxyDto implements UserPersonalMarker {

    LocalDate DOB;

    public UserEditDto(Long id, String name,  String profileName, String email, LocalDate DOB, String userSummary) {
        super(id, name, email, profileName, userSummary);
        this.DOB = DOB;
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

}
