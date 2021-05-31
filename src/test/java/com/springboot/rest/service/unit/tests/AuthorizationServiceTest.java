package com.springboot.rest.service.unit.tests;

import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.config.security.jwt.JwtTokenUtil;
import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.auth.AuthResponse;
import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.SecureResource;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.repository.SecureResourceRepos;
import com.springboot.rest.repository.UserRepos;
import com.springboot.rest.service.AuthorizationService;
import com.springboot.rest.service.unit.data.AbstractDataFactory;
import com.springboot.rest.service.unit.data.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    UserRepos userRepos;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @Mock
    SecureResourceRepos secureResourceRepos;
    @Mock
    SecurityUtils securityUtils;
    @Mock
    AuthResponse authResponse;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    AuthorizationService authorizationService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        UserTestDataFactory userTestDataFactory = AbstractDataFactory.provideUserTestDataFactory();
        user = userTestDataFactory.getRandomUser();
        userDto = userTestDataFactory.getRandomUserDto();
        authResponse = new AuthResponse(new UserProxyDto(user.getId(), user.getName(),
                user.getEmail(), user.getUserSummary(), user.getProfileName()), "xyz");
    }

    @Test
    void loginPass() {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserAdapter userAdapter = new UserAdapter(user);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAdapter);
        when(jwtTokenUtil.generateToken(userAdapter)).thenReturn("xyz");

        assertEquals(authResponse,
                authorizationService.login(new AuthRequest(userDto.getEmail(),
                        userDto.getPassword())));
    }

    @Test
    void loadUserByUsername() {
        when(userRepos.findUserByEmail(anyString())).thenReturn(java.util.Optional.ofNullable(user));
        UserDetails userDetails = authorizationService.loadUserByUsername(anyString());
        verify(userRepos, times(1)).findUserByEmail(anyString());
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void saveSecureResource() {
        boolean returnValue;
        when(securityUtils.getUserFromSubject()).thenReturn(user);
        returnValue = authorizationService.saveSecureResource(1L, 'P');
        assertTrue(returnValue);
        returnValue = authorizationService.saveSecureResource(1L, 'C');
        assertTrue(returnValue);
        returnValue = authorizationService.saveSecureResource(1L, 'L');
        assertTrue(returnValue);
        returnValue = authorizationService.saveSecureResource(1L, 'Q');
        assertTrue(returnValue);
        verify(secureResourceRepos, times(4)).save(any(SecureResource.class));
    }

    @Test
    void deleteSecureResource() {
        boolean returnValue;
        when(securityUtils.getUserFromSubject()).thenReturn(user);
        returnValue = authorizationService.deleteSecureResource(1L, 'P');
        verify(secureResourceRepos, times(1)).deleteDistinctByPost_Id(anyLong());
        assertTrue(returnValue);
        returnValue = authorizationService.deleteSecureResource(1L, 'C');
        verify(secureResourceRepos, times(1)).deleteDistinctByComment_Id(anyLong());
        assertTrue(returnValue);
        returnValue = authorizationService.deleteSecureResource(1L, 'L');
        verify(secureResourceRepos, times(1)).deleteDistinctByLikePost_IdAndOwner_Id(anyLong(), anyLong());
        assertTrue(returnValue);
        returnValue = authorizationService.deleteSecureResource(1L, 'Q');
        verify(secureResourceRepos, times(1)).deleteDistinctByLikeComment_IdAndOwner_Id(anyLong(), anyLong());
        assertTrue(returnValue);
    }
}