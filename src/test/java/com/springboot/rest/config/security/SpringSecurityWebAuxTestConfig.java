package com.springboot.rest.config.security;

import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashMap;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean(name = "basicUsers")
    @Primary
    public static UserDetailsService userDetailsService(@Qualifier("testUsers") HashMap<String, User> userHashMap) {
        return new InMemoryUserDetailsManager(new UserAdapter(userHashMap.get("LOGGED_IN_USER")), new UserAdapter(userHashMap.get("ANOTHER_USER")),
                                                                new UserAdapter(userHashMap.get("THIRD_USER")));
    }
}
