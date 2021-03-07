package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.projections.UserView;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RolesAllowed("ROLE_USER")
    @GetMapping(path = "api/v1/profile/{profileName}")
    public UserView getUser(@RequestBody Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping(path = "api/v1/public/register")
    public ResponseEntity<ApiMessageResponse> registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RolesAllowed("ROLE_USER")
    @DeleteMapping(path = "api/v1/profile/{profileName}")
    public ResponseEntity<ApiMessageResponse> deleteUser(@RequestBody User user) {
        return userService.delUser(user);
    }

    @RolesAllowed("ROLE_USER")
    @PutMapping(path = "api/v1/profile/{profileName}")
    public ResponseEntity<ApiMessageResponse> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

}
