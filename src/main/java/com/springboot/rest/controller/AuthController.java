package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.AuthRequest;
import com.springboot.rest.model.dto.AuthResponse;
import com.springboot.rest.service.AuthorizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController

public class AuthController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/api/v1/public/login")
    @ApiOperation(value = "Login", notes = "Email ID and password required",responseContainer = "ResponseEntity", response = AuthResponse.class)
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest) {
        return authorizationService.login(authRequest);
    }

    @RolesAllowed("ROLE_USER")
    @PostMapping("/api/v1/logout")
    @ApiOperation(value = "Logout", notes = "Currently just sends a response message",responseContainer = "ResponseEntity", response = ApiMessageResponse.class)
    public ResponseEntity<ApiMessageResponse> logoutUser() {
        return authorizationService.logout();
    }

}
