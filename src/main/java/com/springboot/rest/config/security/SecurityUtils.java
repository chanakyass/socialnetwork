package com.springboot.rest.config.security;

import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.repository.UserRepos;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepos userRepos;

    @Autowired
    public SecurityUtils(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

    public UserDetails getUserService() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getSubjectId() {
        UserDetails userDetails =  getUserService();
        User user = userRepos.findUserByEmail(userDetails.getUsername()).orElseThrow(()->new ApiAccessException("Wrong credentials"));
        return user.getId();
    }

    public User getUserFromSubject()
    {
        UserDetails userDetails = getUserService();
        return userRepos.findUserByEmail(userDetails.getUsername()).orElseThrow(()->new ApiAccessException("Wrong credentials"));
    }

    public String getSubjectUsername() {
        UserDetails userDetails =  getUserService();
        return userDetails.getUsername();
    }

}
