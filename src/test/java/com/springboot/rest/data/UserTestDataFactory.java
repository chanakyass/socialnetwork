package com.springboot.rest.data;

import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserTestDataFactory {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final HashMap<String, User> userHashMap;

    @Autowired
    public UserTestDataFactory(UserService userService, SecurityUtils securityUtils, @Qualifier("testUsers") HashMap<String, User> userHashMap)
    {
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.userHashMap = userHashMap;
    }

    public User getLoggedInUser()
    {
        return securityUtils.getUserFromSubject();
    }

    public User getOtherThanLoggedInUser()
    {
        return userService.getUserByEmail(TestBeansConfig.getOtherUserInContext().getEmail());

    }

    public User createUserForTest(String purpose)
    {
        return userHashMap.get(purpose);
    }

}
