package com.springboot.rest.config.security;

import com.springboot.rest.model.entities.User;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserService userService;

    @Autowired
    public SecurityUtils(UserService userService) {
        this.userService = userService;
    }

    public UserDetails getUserService() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getSubjectId() {
        UserDetails userDetails =  getUserService();
        return userService.getUserByEmail(userDetails.getUsername()).getId();
    }

    public User getUserFromSubject()
    {
        UserDetails userDetails = getUserService();
        return userService.getUserByEmail(userDetails.getUsername());
    }

    public String getSubject() {
        UserDetails userDetails =  getUserService();
        return userDetails.getUsername();
    }

}
