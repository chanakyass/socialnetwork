package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.dto.UserEditDto;
import com.springboot.rest.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "User details related API endpoints")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "api/v1/public/register")
    public ResponseEntity<ApiMessageResponse> registerUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @PutMapping(path = "api/v1/profile/{profileId}")
    public ResponseEntity<ApiMessageResponse> updateUser(@RequestBody UserEditDto userEditDto) {
        return userService.updateUser(userEditDto);
    }

    @GetMapping(path = "api/v1/profile/{profileId}")
    public UserDto getUser(@RequestBody Long profileId) {
        return userService.getUser(profileId);
    }

    @DeleteMapping(path = "api/v1/profile/{profileId}")
    public ResponseEntity<ApiMessageResponse> deleteUser(@PathVariable Long profileId) {
        return userService.delUser(profileId);
    }

}
