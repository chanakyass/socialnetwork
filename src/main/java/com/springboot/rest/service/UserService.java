package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.model.projections.UserView;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepos userRepos, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepos = userRepos;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @RolesAllowed("ROLE_USER")
    @GetMapping(path = "api/v1/profile/{profileName}")
    public UserView getUser(Long userId) {
        return userRepos.findUserById(userId).orElseThrow(() -> new ApiSpecificException("User is not present"));
    }

    public User getUserByEmail(String email) {
        return userRepos.findUserByEmail(email).orElseThrow(() -> new ApiSpecificException("email not present in db"));

    }

    public ResponseEntity<ApiMessageResponse> createUser(User user) {
        if (userRepos.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ApiSpecificException("Email is taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User responseUser = userRepos.save(user);
        return ResponseEntity.ok().body(new ApiMessageResponse(responseUser.getId()));

    }

    @Transactional
    @RolesAllowed("ROLE_USER")
    @PreAuthorize(value = "hasPermission(#user, null)")
    public ResponseEntity<ApiMessageResponse> delUser(User user) {
        userRepos.findById(user.getId()).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userRepos.deleteById(user.getId());
        return ResponseEntity.ok().body(new ApiMessageResponse(user.getId()));

    }

    @Transactional
    @RolesAllowed("ROLE_USER")
    @PreAuthorize(value = "hasPermission(#userDto, null)")
    public ResponseEntity<ApiMessageResponse> updateUser(UserDto userDto) {
        User user = userRepos.findById(userDto.getId()).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userMapper.update(userDto, user);
        return ResponseEntity.ok().body(new ApiMessageResponse(userDto.getId()));
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
