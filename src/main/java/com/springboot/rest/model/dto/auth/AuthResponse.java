package com.springboot.rest.model.dto.auth;

import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "Authentication Response", description = "Contains the user id and the user email")
public class AuthResponse {

    UserProxyDto userProxyDto;
    private String jwtToken;

    public AuthResponse(UserProxyDto userProxyDto, String jwtToken) {
        this.userProxyDto = userProxyDto;
        this.jwtToken = jwtToken;

    }

    public static AuthResponse mapUserToAuthResponse(UserAdapter userAdapter, String jwtToken) {
        User user = userAdapter.getUser();
        return new AuthResponse(new UserProxyDto(user.getId(), user.getName(), user.getEmail(), user.getProfileName(), user.getUserSummary()), jwtToken);
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserProxyDto getUserProxyDto() {
        return userProxyDto;
    }

    public void setUserProxyDto(UserProxyDto userProxyDto) {
        this.userProxyDto = userProxyDto;
    }
}
