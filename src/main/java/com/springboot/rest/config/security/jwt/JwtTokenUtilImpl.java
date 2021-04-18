package com.springboot.rest.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.config.security.SecurityProperties;
import com.springboot.rest.config.security.algo.AlgorithmStrategy;
import com.springboot.rest.model.entities.UserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@EnableConfigurationProperties(SecurityProperties.class)
public class JwtTokenUtilImpl implements JwtTokenUtil {

    private final SecurityProperties securityProperties;

    @Autowired
    public JwtTokenUtilImpl(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public boolean validate(String token) {

        try {
            JWTVerifier verifier = JWT.require(AlgorithmStrategy.getAlgorithm(securityProperties.getStrategy()))
                    .withIssuer(securityProperties.getIssuer())
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            return false;
        }
    }

    public String generateToken(UserAdapter userAdapter) {
        String key = userAdapter.getUsername();
        String token = "";
        try {
            token = JWT.create()
                    .withSubject(+userAdapter.getUserUniqueId()+":"
                            +userAdapter.getUsername()+":"
                            +userAdapter.getProfileName())
                    .withIssuer(securityProperties.getIssuer())
                    .sign(AlgorithmStrategy.getAlgorithm(securityProperties.getStrategy()));
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
            exception.printStackTrace();
        }

        return token;

    }

    @Override
    public String getSubjectFromToken(String token) {

        DecodedJWT decodedJWT = JWT.decode(token);

        return decodedJWT.getSubject();


    }

    @Override
    public String extractTokenAndGetSubject(HttpServletRequest request) {

        String token = Optional.of(extractToken(request)).orElseThrow(()->new ApiAccessException("Empty Token"));
        return getSubjectFromToken(token);
    }

    @Override
    public String extractToken(HttpServletRequest request) {

        // Get authorization header and validate
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (header == null || header.isEmpty() || !header.startsWith("Bearer ")) ?
                null : header.split(" ")[1].trim();

    }
}
