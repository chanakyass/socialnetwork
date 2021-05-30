package com.springboot.rest.integration.tests.data;

import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.integration.tests.config.TestConfig;
import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.auth.AuthResponse;
import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.service.AuthorizationService;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        return userMapper.toUserDto(user);
    }

    public UserDto getOtherThanLoggedInUser()
    {
        User user = userService.getUserByEmail(userHashMap.get("ANOTHER_USER").getEmail());
        return userMapper.toUserDto(user);


    }

    public UserDto getThisUser(String userKey)
    {
        User user = userHashMap.get(userKey);
        return userMapper.toUserDto(user);
    }

    public UserDto createUserForTest()
    {
        Random random = TestConfig.getRandom();
        int num = random.nextInt(1000);
        List<Role> roles = List.of(Role.builder().authority("ROLE_USER").build());
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
        return userService.createUser(userDto);
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
        userDto.setGrantedAuthoritiesList(List.of(Role.builder().authority("ROLE_USER").build()));


        assertNotNull(userService.createUser(userDto));
        return userDto;
    }

    public String loginAndGetJwtToken(AuthRequest authRequest)
    {
        AuthResponse authResponse = auth.login(authRequest);
        return Objects.requireNonNull(authResponse.getJwtToken());

    }

}
