package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.config.security.jwt.JwtTokenUtil;
import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.auth.AuthResponse;
import com.springboot.rest.model.dto.response.ApiMessageResponse;
import com.springboot.rest.model.entities.*;
import com.springboot.rest.repository.SecureResourceRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public AuthResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();
            return AuthResponse.mapUserToAuthResponse(userAdapter, jwtTokenUtil.generateToken(userAdapter));

        } catch (BadCredentialsException badCredentialsException) {
            throw new ApiAccessException("Incorrect credentials.");
        }
    }

    public ResponseEntity<ApiMessageResponse> logout() {
        return ResponseEntity.ok().body(new ApiMessageResponse("Thank you for using our services. Visit again!"));
    }

    @Transactional
    public boolean saveSecureResource(Long id, char type)
    {
        User user = securityUtils.getUserFromSubject();
        SecureResource.SecureResourceBuilder secureResourceBuilder = SecureResource.builder().id(null).owner(user).type(type);
        switch (type)
        {
            case 'P': {
                Post post = new Post();
                post.setId(id);
                secureResourceBuilder.post(post).build();
                break;
            }
            case 'C': {
                Comment comment = new Comment();
                comment.setId(id);
                secureResourceBuilder.comment(comment).build();
                break;
            }
            case 'L':{

                Post post = new Post();
                post.setId(id);
                secureResourceBuilder.likePost(post).build();
                break;
            }
            case 'Q': {

                Comment comment = new Comment();
                comment.setId(id);
                secureResourceBuilder.likeComment(comment).build();
                break;
            }
            default: {
                return false;
            }

        }

        secureResourceRepos.save(secureResourceBuilder.build());
        return true;
    }

    @Transactional
    public boolean deleteSecureResource(Long id, char type) {
        User user = securityUtils.getUserFromSubject();
        SecureResource secureResource = null;

        try {
            switch (type) {
                case 'P': {
                    secureResourceRepos.deleteDistinctByPost_Id(id);
                    break;
                }
                case 'C': {
                    secureResourceRepos.deleteDistinctByComment_Id(id);
                    break;
                }
                case 'L': {
                    secureResourceRepos.deleteDistinctByLikePost_IdAndOwner_Id(id, user.getId());
                    break;
                }
                case 'Q': {
                    secureResourceRepos.deleteDistinctByLikeComment_IdAndOwner_Id(id, user.getId());
                    break;
                }
                default: {
                    return false;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }



    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepos.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Incorrect credentials"));
        return new UserAdapter(user);
    }
}
