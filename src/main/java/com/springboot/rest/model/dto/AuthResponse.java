package com.springboot.rest.model.dto;

import com.springboot.rest.model.entities.UserAdapter;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "Authentication Response", description = "Contains the user id and the user email")
public class AuthResponse {

    private Long id;
    private String username;

    public AuthResponse(UserAdapter userAdapter) {
        this.username = userAdapter.getUsername();
        this.id = userAdapter.getUserUniqueId();

    }

    public static AuthResponse mapUserToAuthResponse(UserAdapter userAdapter) {
        return new AuthResponse(userAdapter);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
