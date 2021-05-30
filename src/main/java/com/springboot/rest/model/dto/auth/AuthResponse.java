package com.springboot.rest.model.dto.auth;

import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Authentication Response", description = "Contains the user id and the user email")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    UserProxyDto userProxyDto;
    private String jwtToken;

    public static AuthResponse mapUserToAuthResponse(UserAdapter userAdapter, String jwtToken) {
        User user = userAdapter.getUser();
        return new AuthResponse(new UserProxyDto(user.getId(), user.getName(), user.getEmail(), user.getProfileName(), user.getUserSummary()), jwtToken);
    }
}
