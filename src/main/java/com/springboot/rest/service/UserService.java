package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.UserEdit;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.model.projections.UserView;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<ApiMessageResponse> delUser(User user) {
        userRepos.findById(user.getId()).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userRepos.deleteById(user.getId());
        return ResponseEntity.ok().body(new ApiMessageResponse(user.getId()));

    }

    @Transactional
    public ResponseEntity<ApiMessageResponse> updateUser(UserEdit userEdit) {
        User user = userRepos.findById(userEdit.getId()).orElseThrow(() -> new ApiSpecificException("User is not present"));
        userMapper.update(userEdit, user);
        return ResponseEntity.ok().body(new ApiMessageResponse(userEdit.getId()));
    }

    @Transactional
    public ResponseEntity<ApiMessageResponse> delAllUsers()
    {
        if(!userRepos.findAll().isEmpty())
        {
            userRepos.deleteAll();
        }
        return ResponseEntity.ok().body(new ApiMessageResponse());
    }

}
