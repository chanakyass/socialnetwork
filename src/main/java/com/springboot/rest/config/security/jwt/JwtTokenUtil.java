package com.springboot.rest.config.security.jwt;

import com.springboot.rest.model.entities.UserAdapter;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenUtil {

    String extractTokenAndGetSubject(HttpServletRequest request);

    String extractToken(HttpServletRequest request);

    boolean validate(String token);

    String getSubjectFromToken(String token);

    String generateToken(UserAdapter userAdapter);

}
