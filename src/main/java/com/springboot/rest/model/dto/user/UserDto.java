package com.springboot.rest.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.rest.model.dto.UserPersonalMarker;
import com.springboot.rest.model.entities.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@ApiModel(value = "User", description = "Contains only the additional details of the user", parent = UserProxyDto.class)
public class UserDto extends UserProxyDto implements UserPersonalMarker {


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer age;

    LocalDate DOB;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Role> grantedAuthoritiesList;

    public UserDto(Long id, String name, String profileName, String email, String password, LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        super(id, name,  email, profileName, userSummary);
        this.profileName = profileName;
        this.password = password;
        this.DOB = DOB;
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

    public List<Role> getGrantedAuthoritiesList() {
        return grantedAuthoritiesList;
    }

    public void setGrantedAuthoritiesList(List<Role> grantedAuthoritiesList) {
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }


}
