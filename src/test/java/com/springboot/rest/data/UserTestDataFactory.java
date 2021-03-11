package com.springboot.rest.data;

import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.TestConfig;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    public User getUserWithNeed(String need)
    {
        return userHashMap.get(need);
    }

    public User createUserForTest()
    {
        /*

            "name": "swathy",
            "email": "swathy@kothi.com",
            "password": "pass",
            "grantedAuthoritiesList": [{
            "authority":"ROLE_USER"}]

         */
        Random random = TestConfig.getRandom();
        int num = random.nextInt(1000);
        List<Role> roles = List.of(Role.roleBuilder().authority("ROLE_USER").build());
        return User.builder().name("swathy"+ num).email("swathy"+num+"@kothi.com").password("pass").grantedAuthoritiesList(roles).build();
    }

}
