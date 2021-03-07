package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.AuthRequest;
import com.springboot.rest.model.dto.AuthResponse;
import com.springboot.rest.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController

public class AuthController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @RequestMapping("/api/v1/public/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest) {
        return authorizationService.login(authRequest);
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping("/api/v1/{username}/logout")
    public ResponseEntity<ApiMessageResponse> logoutUser() {
        return authorizationService.logout();
    }

}
