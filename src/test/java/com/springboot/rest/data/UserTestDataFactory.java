package com.springboot.rest.data;

import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.TestConfig;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.AuthRequest;
import com.springboot.rest.model.dto.AuthResponse;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.service.AuthorizationService;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Service
public class UserTestDataFactory {

    private final UserService userService;
    private final AuthorizationService auth;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;
    private final HashMap<String, User> userHashMap;

    @Autowired
    public UserTestDataFactory(UserService userService, AuthorizationService auth, SecurityUtils securityUtils,
                               UserMapper userMapper, @Qualifier("testUsers") HashMap<String, User> userHashMap)
    {
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.userHashMap = userHashMap;
        this.userMapper = userMapper;
        this.auth = auth;
    }

    public UserDto getLoggedInUser()
    {
        User user  = securityUtils.getUserFromSubject();
        UserDto userDto = new UserDto();
        userMapper.toUserDto(user, userDto);
        return userDto;
    }

    public UserDto getOtherThanLoggedInUser()
    {
        User user = userService.getUserByEmail(TestBeansConfig.getOtherUserInContext().getEmail());
        UserDto userDto = new UserDto();
        userMapper.toUserDto(user, userDto);
        return userDto;


    }

    public UserDto getThisUser(String userKey)
    {
        User user = userHashMap.get(userKey);
        UserDto userDto = new UserDto();
        userMapper.toUserDto(user, userDto);
        return userDto;
    }

    public UserDto createUserForTest()
    {
        Random random = TestConfig.getRandom();
        int num = random.nextInt(1000);
        List<Role> roles = List.of(Role.roleBuilder().authority("ROLE_USER").build());
        UserDto userDto = new UserDto();
        userDto.setName("swathy"+num);
        userDto.setEmail("swathy"+num+"@kothi.com");
        userDto.setPassword("pass");
        userDto.setGrantedAuthoritiesList(roles);
        return userDto;
    }

    public Long createUserForTestInDB()
    {
        UserDto userDto = createUserForTest();
        ResponseEntity<ApiMessageResponse> message = userService.createUser(userDto);
        return Objects.requireNonNull(message.getBody()).getResourceId();
    }

    public UserDto getUserFromDBWithId(Long id)
    {
        return userService.getUser(id);
    }

    public UserDto createUserWithCreds(String email, String password)
    {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setName("random");
        userDto.setGrantedAuthoritiesList(List.of(Role.roleBuilder().authority("ROLE_USER").build()));


        assertEquals(userService.createUser(userDto).getStatusCode(), HttpStatus.OK);
        return userDto;
    }

    public String loginAndGetJwtToken(AuthRequest authRequest)
    {
        ResponseEntity<AuthResponse> message = auth.login(authRequest);
        HttpHeaders http = message.getHeaders();
        return Objects.requireNonNull(http.get("Authorization")).get(0);

    }

}
