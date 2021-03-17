package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.Data;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.dto.UserEditDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserEditMapper;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final UserEditMapper userEditMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepos userRepos, PasswordEncoder passwordEncoder, UserEditMapper userEditMapper, UserMapper userMapper) {
        this.userRepos = userRepos;
        this.passwordEncoder = passwordEncoder;
        this.userEditMapper = userEditMapper;
        this.userMapper = userMapper;
    }

    @RolesAllowed("ROLE_USER")
    @GetMapping(path = "api/v1/profile/{profileName}")
    public Data<UserDto> getUser(Long userId) {
        User user = userRepos.findUserById(userId).orElseThrow(() -> new ApiSpecificException("User is not present"));
        UserDto userDto = new UserDto();
        userMapper.toUserDto(user, userDto);
        return new Data<>(userDto);
    }

    public User getUserByEmail(String email) {
        return userRepos.findUserByEmail(email).orElseThrow(() -> new ApiSpecificException("email not present in db"));
    }

    public Long createUser(UserDto userDto) {
        User user = new User();
        userMapper.toUser(userDto, user);

        if (userRepos.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ApiSpecificException("Email is taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User responseUser = userRepos.save(user);
        return responseUser.getId();

    }

    @Transactional
    @RolesAllowed("ROLE_USER")
    @PreAuthorize(value = "hasPermission(#profileId, \"UserPersonalMarker\", null)")
    public Long delUser(Long profileId) {


        userRepos.findById(profileId).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userRepos.deleteById(profileId);
        return profileId;

    }

    @Transactional
    @RolesAllowed("ROLE_USER")
    @PreAuthorize(value = "hasPermission(#userEditDto, null)")
    public Long updateUser(UserEditDto userEditDto) {

        User user = userRepos.findById(userEditDto.getId()).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userEditMapper.update(userEditDto, user);
        return userEditDto.getId();
    }

    @Transactional
    public void delAllUsers()
    {
        if(!userRepos.findAll().isEmpty())
        {
            userRepos.deleteAll();
        }
    }

}
