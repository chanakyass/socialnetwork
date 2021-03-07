package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.config.security.jwt.JwtTokenUtil;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.AuthRequest;
import com.springboot.rest.model.dto.AuthResponse;
import com.springboot.rest.model.entities.SecureResource;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import com.springboot.rest.repository.SecureResourceRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private UserRepos userRepos;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private SecureResourceRepos secureResourceRepos;
    private SecurityUtils securityUtils;

    public UserRepos getUserRepos() {
        return userRepos;
    }

    @Autowired
    public void setUserRepos(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public JwtTokenUtil getJwtTokenUtil() {
        return jwtTokenUtil;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public SecureResourceRepos getSecureResourceRepos() {
        return secureResourceRepos;
    }

    @Autowired
    public void setSecureResourceRepos(SecureResourceRepos secureResourceRepos) {
        this.secureResourceRepos = secureResourceRepos;
    }

    public SecurityUtils getSecurityUtils() {
        return securityUtils;
    }

    @Autowired
    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(userAdapter))
                    .body(AuthResponse.mapUserToAuthResponse(userAdapter));


        } catch (BadCredentialsException badCredentialsException) {
            throw new ApiAccessException("Incorrect credentials.");
        }
    }

    public ResponseEntity<ApiMessageResponse> logout() {
        return ResponseEntity.ok().body(new ApiMessageResponse("Thank you for using our services. Visit again!"));
    }

    public boolean saveSecureResource(Long id)
    {
        User user = securityUtils.getUserFromSubject();
        SecureResource secureResource = new SecureResource(id, user);
        secureResourceRepos.save(secureResource);
        return true;
    }

    public boolean deleteSecureResource(Long id)
    {
        User user = securityUtils.getUserFromSubject();
        secureResourceRepos.deleteById(id);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepos.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Incorrect credentials"));
        return new UserAdapter(user);
    }
}
