package com.springboot.rest.unit.tests.services;

import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.LikeComment;
import com.springboot.rest.model.entities.LikePost;
import com.springboot.rest.model.mapper.LikeCommentMapper;
import com.springboot.rest.model.mapper.LikePostMapper;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.LikeCommentRepos;
import com.springboot.rest.repository.LikePostRepos;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.service.LikeService;
import com.springboot.rest.unit.data.AbstractDataFactory;
import com.springboot.rest.unit.data.LikeTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikePostRepos likePostRepos;
    @Mock
    private LikeCommentRepos likeCommentRepos;
    @Mock
    private PostRepos postRepos;
    @Mock
    private CommentRepos commentRepos;
    @Mock
    private LikePostMapper likePostMapper;
    @Mock
    private LikeCommentMapper likeCommentMapper;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private LikeService likeService;

    private LikePost likePost;
    private LikeComment likeComment;
    private LikePostDto likePostDto;
    private LikeCommentDto likeCommentDto;
    private List<LikePost> likePostList;
    private List<LikeComment> likeCommentList;
    private List<LikePostDto> likePostDtoList;
    private List<LikeCommentDto> likeCommentDtoList;


    @BeforeEach
    void setUp() {
        LikeTestDataFactory likeTestDataFactory = AbstractDataFactory.provideLikeTestDataFactory();
        likePost = likeTestDataFactory.getRandomLikePost();
        likeComment = likeTestDataFactory.getRandomLikeComment();
        likePostDto = likeTestDataFactory.getRandomLikePostDto();
        likeCommentDto = likeTestDataFactory.getRandomLikeCommentDto();
        likePostDtoList = likeTestDataFactory.getLikePostDtoList();
        likeCommentDtoList = likeTestDataFactory.getLikeCommentDtoList();
        likePostList = likeTestDataFactory.getLikePostList();
        likeCommentList = likeTestDataFactory.getLikeCommentList();
    }

    @Test
    void likeAPost() {
        when(likePostMapper.toLikePost(likePostDto)).thenReturn(likePost);

        when(postRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likePost.getLikedPost()));

        when(likePostRepos.findLikeByLikedPost_IdAndOwner_Id(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        when(likePostRepos.save(likePost)).thenReturn(likePost);
        Long likedPostId = likeService.likeAPost(likePostDto);

        verify(likePostRepos, times(1)).save(any(LikePost.class));

        assertEquals(likePost.getLikedPost().getId(), likedPostId);
    }

    @Test
    void likeComment() {
        when(likeCommentMapper.toLikeComment(likeCommentDto)).thenReturn(likeComment);

        when(commentRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likeComment.getLikedComment()));
        when(likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        when(likeCommentRepos.save(likeComment)).thenReturn(likeComment);
        Long likeCommentId = likeService.likeComment(likeCommentDto);

        verify(likeCommentRepos, times(1)).save(any(LikeComment.class));

        assertEquals(likeComment.getLikedComment().getId(), likeCommentId);
    }

    @Test
    void unlikePost() {
        when(likePostMapper.toLikePost(likePostDto)).thenReturn(likePost);

        when(postRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likePost.getLikedPost()));
        when(likePostRepos.findLikeByLikedPost_IdAndOwner_Id(anyLong(), anyLong()))
                .thenReturn(Optional.ofNullable(likePost));

        Long unlikedPostId = likeService.unlikePost(likePostDto);

        verify(likePostRepos, times(1)).deleteById(anyLong());

        assertEquals(likePost.getLikedPost().getId(), unlikedPostId);
    }

    @Test
    void unlikeComment() {
        when(likeCommentMapper.toLikeComment(likeCommentDto)).thenReturn(likeComment);

        when(commentRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likeComment.getLikedComment()));
        when(likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(anyLong(), anyLong()))
                .thenReturn(Optional.ofNullable(likeComment));

        Long unlikedCommentId = likeService.unlikeComment(likeCommentDto);

        verify(likeCommentRepos, times(1)).deleteById(anyLong());

        assertEquals(likeComment.getLikedComment().getId(), unlikedCommentId);
    }

    @Test
    void getLikesOnPost() {
        when(postRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likePost.getLikedPost()));
        when(likePostRepos.findLikesByLikedPost_Id(anyLong()))
                .thenReturn(Optional.ofNullable(likePostList));
        when(likePostMapper.toLikePostDtoList(likePostList))
                .thenReturn(likePostDtoList);
        DataList<LikePostDto> list = likeService.getLikesOnPost(anyLong());
        verify(likePostRepos, times(1))
                .findLikesByLikedPost_Id(anyLong());
        assertEquals(likePostDtoList, list.getDataList());
    }

    @Test
    void getLikesOnComment() {

        when(commentRepos.findById(anyLong()))
                .thenReturn(Optional.ofNullable(likeComment.getLikedComment()));
        when(likeCommentRepos.findLikesByLikedComment_Id(anyLong()))
                .thenReturn(Optional.ofNullable(likeCommentList));
        when(likeCommentMapper.toLikeCommentDtoList(likeCommentList))
                .thenReturn(likeCommentDtoList);
        DataList<LikeCommentDto> list = likeService.getLikesOnComment(anyLong());
        verify(likeCommentRepos, times(1))
                .findLikesByLikedComment_Id(anyLong());
        assertEquals(likeCommentDtoList, list.getDataList());
    }

}