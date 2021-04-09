package com.springboot.rest.controller;

import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.auth.AuthResponse;
import com.springboot.rest.model.dto.response.ApiMessageResponse;
import com.springboot.rest.model.dto.response.Data;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.service.AuthorizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Data<UserProxyDto>> loginUser(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authorizationService.login(authRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authResponse.getJwtToken())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*", HttpHeaders.AUTHORIZATION)
                .body(new Data<>(authResponse.getUserProxyDto()));
        //return new Data<>(authorizationService.login(authRequest));
    }

    @RolesAllowed("ROLE_USER")
    @PostMapping("/api/v1/logout")
    @ApiOperation(value = "Logout", notes = "Currently just sends a response message",responseContainer = "ResponseEntity", response = ApiMessageResponse.class)
    public ResponseEntity<ApiMessageResponse> logoutUser() {
        return authorizationService.logout();
    }

}
