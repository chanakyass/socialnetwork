package com.springboot.rest.unit.data;

import com.springboot.rest.model.dto.comment.CommentProxyDto;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.post.PostProxyDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LikeTestDataFactory {
    private final PostTestDataFactory postTestDataFactory;
    private final CommentTestDataFactory commentTestDataFactory;
    private final UserTestDataFactory userTestDataFactory;

    public LikeTestDataFactory(PostTestDataFactory postTestDataFactory, CommentTestDataFactory commentTestDataFactory, UserTestDataFactory userTestDataFactory) {
        this.postTestDataFactory = postTestDataFactory;
        this.commentTestDataFactory = commentTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
    }

    public LikePost getRandomLikePost(){
        Post post = postTestDataFactory.getRandomPost();
        User owner = userTestDataFactory.getRandomOtherUser();
        return new LikePost(1L, owner, LocalDateTime.now(), post);
    }

    public LikeComment getRandomLikeComment(Long i){
        Comment comment = commentTestDataFactory.getRandomComment();
        User owner = userTestDataFactory.getRandomUser(i);
        return new LikeComment(1L, owner, LocalDateTime.now(), comment);
    }

    public LikePost getRandomLikePost(Long i){
        Post post = postTestDataFactory.getRandomPost();
        User owner = userTestDataFactory.getRandomUser(i);
        return new LikePost(1L, owner, LocalDateTime.now(), post);
    }

    public LikeComment getRandomLikeComment(){
        Comment comment = commentTestDataFactory.getRandomComment();
        User owner = userTestDataFactory.getRandomOtherUser();
        return new LikeComment(1L, owner, LocalDateTime.now(), comment);
    }

    public LikePostDto getRandomLikePostDto(){
        PostProxyDto postDto = postTestDataFactory.getRandomPostDto();
        UserProxyDto userDto = userTestDataFactory.getRandomOtherUserProxyDto();
        return new LikePostDto(1L, userDto, LocalDateTime.now(), postDto);
    }

    public LikeCommentDto getRandomLikeCommentDto(){
        CommentProxyDto commentProxyDto = commentTestDataFactory.getRandomCommentDto();
        UserProxyDto userDto = userTestDataFactory.getRandomOtherUserProxyDto();
        return new LikeCommentDto(1L, userDto, LocalDateTime.now(), commentProxyDto);
    }

    private LikePostDto getRandomLikePostDto(Long i){
        PostProxyDto postDto = postTestDataFactory.getRandomPostDto();
        UserProxyDto userDto = userTestDataFactory.getRandomUserProxyDto(i);
        return new LikePostDto(i, userDto, LocalDateTime.now(), postDto);
    }

    private LikeCommentDto getRandomLikeCommentDto(Long i){
        CommentProxyDto commentProxyDto = commentTestDataFactory.getRandomCommentDto();
        UserProxyDto userProxyDto = userTestDataFactory.getRandomUserProxyDto(i);
        return new LikeCommentDto(i, userProxyDto, LocalDateTime.now(), commentProxyDto);
    }

    public List<LikePostDto> getLikePostDtoList(){
        List<LikePostDto> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomLikePostDto(i));
        }
        return list;
    }

    public List<LikeCommentDto> getLikeCommentDtoList(){
        List<LikeCommentDto> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomLikeCommentDto(i));
        }
        return list;
    }

    public List<LikePost> getLikePostList(){
        List<LikePost> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomLikePost(i));
        }
        return list;
    }

    public List<LikeComment> getLikeCommentList(){
        List<LikeComment> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomLikeComment(i));
        }
        return list;
    }
}
