package com.springboot.rest.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "User proxy structure", description = "Contains the id, name and email of user")
public class UserProxyDto {

    Long id;
    String name;
    String email;
    String userSummary;
    String profileName;

    public UserProxyDto(Long id, String name, String email, String profileName, String userSummary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userSummary = userSummary;
        this.profileName = profileName;
    }

    public UserProxyDto() {
    }

    @JsonSerialize(as = Long.class)
    public Long getId() {
        return id;
    }

    @JsonDeserialize(as=Long.class)

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

    public String getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(String userSummary) {
        this.userSummary = userSummary;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProxyDto)) return false;
        UserProxyDto that = (UserProxyDto) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getUserSummary());
    }
}
