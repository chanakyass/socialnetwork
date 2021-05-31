package com.springboot.rest.service.unit.tests;

import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserEditDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserEditMapper;
import com.springboot.rest.model.mapper.UserEditMapperImpl;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.repository.UserRepos;
import com.springboot.rest.service.UserService;
import com.springboot.rest.service.unit.data.AbstractDataFactory;
import com.springboot.rest.service.unit.data.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest{

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepos userRepos;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserEditMapper userEditMapper;

    private User user;

    private UserDto userDto;

    private UserEditDto userEditDto;

    @BeforeEach
    void setUp() {

        UserTestDataFactory userTestDataFactory = AbstractDataFactory.provideUserTestDataFactory();

        user = userTestDataFactory.getRandomUser();

        userDto = userTestDataFactory.getRandomUserDto();

        userEditDto = userTestDataFactory.getRandomEditedUserDto();

    }

    @Test
    void getUser() {
        when(userRepos.findUserById(userDto.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        assertEquals(userDto, userService.getUser(userDto.getId()));
    }

    @Test
    void getUserByEmail() {
        when(userRepos.findUserByEmail(anyString()))
                .thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserByEmail(userDto.getEmail()));
    }

    @Test
    void getUserByIdAndEmail() {

    }

    @Test
    void createUser() {

        when(userMapper.toUser(userDto)).thenReturn(user);


        when(userRepos.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        when(userRepos.save(user))
                .thenReturn(user);

        assertEquals(user.getId(), userService.createUser(userDto));
    }

    @Test
    void delUser() {
        when(userRepos.findById(userDto.getId())).thenReturn(Optional.ofNullable(user));
        assertEquals(user.getId(), userService.delUser(userDto.getId()));
    }

    @Test
    void updateUser() {
        when(userRepos.findById(userDto.getId())).thenReturn(Optional.ofNullable(user));
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();

            (new UserEditMapperImpl()).update((UserEditDto) args[0], (User) args[1]);
            return null;
        }).when(userEditMapper).update(userEditDto, user);
        assertEquals(user.getId(), userService.updateUser(userEditDto));
    }
}