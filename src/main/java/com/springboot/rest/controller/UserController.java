package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.AuthResponse;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.dto.UserEditDto;
import com.springboot.rest.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Register the user", notes = "User details to be provided in the payload",responseContainer = "ResponseEntity", response = ApiMessageResponse.class)
    public ResponseEntity<ApiMessageResponse> registerUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @PutMapping(path = "api/v1/profile/{profileId}")
    @ApiOperation(value = "Update the user details",
            notes = "Details for update to be provided as part of payload. Currently doesnt support password modification",
            responseContainer = "ResponseEntity", response = ApiMessageResponse.class)
    public ResponseEntity<ApiMessageResponse> updateUser(@RequestBody UserEditDto userEditDto) {
        return userService.updateUser(userEditDto);
    }

    @ApiOperation(value = "Gets the user details",
            notes = "Users profile id to be provided as path variable"
            , response = UserDto.class)

    @GetMapping(path = "api/v1/profile/{profileId}")
    public UserDto getUser(@RequestBody Long profileId) {
        return userService.getUser(profileId);
    }

    @ApiOperation(value = "Deletes the user from application",
            notes = "Users profile id to be provided as path variable", responseContainer = "ResponseEntity",response = ApiMessageResponse.class)

    @DeleteMapping(path = "api/v1/profile/{profileId}")
    public ResponseEntity<ApiMessageResponse> deleteUser(@PathVariable Long profileId) {
        return userService.delUser(profileId);
    }

}
