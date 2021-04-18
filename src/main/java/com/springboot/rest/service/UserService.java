package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiResourceNotFoundException;
import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserEditDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserEditMapper;
import com.springboot.rest.model.mapper.UserMapper;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.LikeCommentRepos;
import com.springboot.rest.repository.LikePostRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepos userRepos;
    private final CommentRepos commentRepos;
    private final PasswordEncoder passwordEncoder;
    private final UserEditMapper userEditMapper;
    private final UserMapper userMapper;
    private final LikePostRepos likePostRepos;
    private final LikeCommentRepos likeCommentRepos;

    @Autowired
    public UserService(UserRepos userRepos, CommentRepos commentRepos, LikePostRepos likePostRepos, LikeCommentRepos likeCommentRepos, PasswordEncoder passwordEncoder, UserEditMapper userEditMapper, UserMapper userMapper) {
        this.userRepos = userRepos;
        this.passwordEncoder = passwordEncoder;
        this.userEditMapper = userEditMapper;
        this.userMapper = userMapper;
        this.commentRepos = commentRepos;
        this.likePostRepos = likePostRepos;
        this.likeCommentRepos = likeCommentRepos;
    }

    @RolesAllowed("ROLE_USER")
    public UserDto getUser(Long userId) {
        User user = userRepos.findUserById(userId).orElseThrow(() -> new ApiSpecificException("User is not present"));
        UserDto userDto = new UserDto();
        userMapper.toUserDto(user, userDto);
        return userDto;
    }

    public User getUserByEmail(String email) {
        return userRepos.findUserByEmail(email).orElseThrow(() -> new ApiSpecificException("User doesn't exist"));
    }

    public User getUserByIdAndEmail(Long userId, String email){
        return userRepos.findUserByIdAndEmail(userId, email).orElseThrow(() -> new ApiSpecificException("User doesn't exist"));
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
    @PreAuthorize(value = "hasPermission(#profileId, \"User\", null)")
    public Long delUser(Long profileId) {
        userRepos.findById(profileId).orElseThrow(() -> new ApiResourceNotFoundException("User doesn't exist"));
        commentRepos.findAllCommentsByOwner_Id(profileId).ifPresent(comments -> comments.forEach((comment) -> {
            Post parentPost = comment.getCommentedOn();
            //parentPost.setNoOfComments(parentPost.getNoOfComments() - 1);
            if(comment.getParentComment() != null){
                Comment parentComment = comment.getParentComment();
                parentComment.setNoOfReplies(parentComment.getNoOfReplies() - 1);
            }
        }));

        likePostRepos.findByOwner_Id(profileId).ifPresent(likes -> likes.forEach(like -> {
            Post post = like.getLikedPost();
            //post.setNoOfLikes(post.getNoOfLikes() - 1);
        }));

        likeCommentRepos.findByOwner_Id(profileId).ifPresent(likes -> likes.forEach(like -> {
            Comment comment = like.getLikedComment();
            comment.setNoOfLikes(comment.getNoOfLikes() - 1);
        }));

        userRepos.deleteById(profileId);
        return profileId;

    }

    @Transactional
    @RolesAllowed("ROLE_USER")
    @PreAuthorize(value = "hasPermission(#userEditDto, null)")
    public Long updateUser(UserEditDto userEditDto) {

        User user = userRepos.findById(userEditDto.getId()).orElseThrow(() -> new ApiResourceNotFoundException("User doesn't exist"));
        userEditMapper.update(userEditDto, user);
        return user.getId();
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
