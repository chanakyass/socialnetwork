package com.springboot.rest.unit.tests.services;

import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.PostEditMapper;
import com.springboot.rest.model.mapper.PostEditMapperImpl;
import com.springboot.rest.model.mapper.PostMapper;
import com.springboot.rest.model.mapper.PostMapperImpl;
import com.springboot.rest.model.projections.PostView;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.repository.UserRepos;
import com.springboot.rest.service.PostService;
import com.springboot.rest.unit.data.AbstractDataFactory;
import com.springboot.rest.unit.data.PostTestDataFactory;
import com.springboot.rest.unit.data.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepos postRepos;
    @Mock
    private UserRepos userRepos;
    @Mock
    private SecurityUtils securityUtils;
    @Spy
    PostMapper postMapper = new PostMapperImpl();
    @Spy
    PostEditMapper postEditMapper = new PostEditMapperImpl();
    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;
    private PostDto postDto;
    private PostEditDto postEditDto;
    private List<PostView> postViewList;
    private List<PostDto> postDtoList;
    private PostView postView;

    @BeforeEach
    void setUp() {

        UserTestDataFactory userTestDataFactory = AbstractDataFactory.provideUserTestDataFactory();

        PostTestDataFactory postTestDataFactory = AbstractDataFactory.providePostTestDataFactory();

        user = userTestDataFactory.getRandomUser();

        post = postTestDataFactory.getRandomPost();

        postDto = postTestDataFactory.getRandomPostDto();

        postEditDto = postTestDataFactory.getRandomPostEditDto();

        postViewList = postTestDataFactory.getListOfPostViews();

        postView = postTestDataFactory.getRandomPostView();

        postDtoList = postTestDataFactory.getRandomListOfPostDtos();

    }

    @Test
    void addPost() {
        when(postRepos.save(any())).thenReturn(post);
        when(postMapper.toPost(postDto)).thenReturn(post);
        Long postId = postService.addPost(postDto);
        verify(postRepos, times(1)).save(any(Post.class));
        assertEquals(post.getId(), postId);
    }

    @Test
    void getPosts() {
        @SuppressWarnings("unchecked")
        Class<Page<PostView>> classType = (Class<Page<PostView>>)(Object)Page.class;
        Page<PostView> page = Mockito.mock(classType);
        assert page!=null;

        when(postRepos.findPostsForUserFeed(any(), any())).thenReturn(Optional.of(page));
        when(postMapper.toPostDtoListFromView(postViewList)).thenReturn(postDtoList);
        when(page.getContent()).thenReturn(postViewList);
        when(page.getTotalPages()).thenReturn(postViewList.size());
        DataList<PostDto> dataList = postService.getPosts(0, 0);
        verify(postRepos, times(1)).findPostsForUserFeed(anyLong(), any(Pageable.class));
        assertEquals(postDtoList, dataList.getDataList());
    }

    @Test
    void getSelectedPost() {
        when(postRepos.findPostWithId(anyLong(), anyLong())).thenReturn(Optional.ofNullable(postView));
        when(postMapper.toPostDtoFromView(postView)).thenReturn(postDto);
        PostDto selectedPostDto = postService.getSelectedPost(1L);
        verify(postRepos, times(1)).findPostWithId(anyLong(), anyLong());
        assertEquals(postDto, selectedPostDto);
    }

    @Test
    void getPostsOfUser() {
        @SuppressWarnings("unchecked")
        Class<Page<PostView>> classType = (Class<Page<PostView>>)(Object)Page.class;
        Page<PostView> page = Mockito.mock(classType);
        assert page!=null;
        when(userRepos.findById(eq(1L))).thenReturn(Optional.ofNullable(user));
        when(postRepos.findPostsOfOwner(eq(1L), anyLong(), any(Pageable.class))).thenReturn(Optional.of(page));
        List<PostView> filteredPostViewList = postViewList.stream()
                .filter(view-> view.getPost().getOwner().getId().equals(1L)).collect(Collectors.toList());
        List<PostDto> filteredPostDtoList = postDtoList.stream()
                .filter(dto->dto.getOwner().getId().equals(1L)).collect(Collectors.toList());
        when(page.getContent()).thenReturn(filteredPostViewList);
        when(page.getTotalPages()).thenReturn(postViewList.size());
        when(postMapper.toPostDtoListFromView(filteredPostViewList)).thenReturn(filteredPostDtoList);
        DataList<PostDto> dataList = postService.getPostsOfUser(1L, 0, 0);
        verify(postRepos, times(1)).findPostsOfOwner(anyLong(), anyLong(), any(Pageable.class));
        assertEquals(filteredPostDtoList, dataList.getDataList());
    }

    @Test
    void updatePost() {
        when(postRepos.findById(postEditDto.getId())).thenReturn(Optional.ofNullable(post));
        doAnswer(invocation -> {
            Object [] args = invocation.getArguments();
            PostTestDataFactory.editPost((PostEditDto) args[0], (Post) args[1]);
            return null;
        }).when(postEditMapper).toPost(postEditDto, post);
        assertEquals(post.getId(), postService.updatePost(postEditDto));
    }

    @Test
    void deletePost() {
        when(postRepos.findById(postDto.getId())).thenReturn(Optional.ofNullable(post));
        Long postId = postService.deletePost(postDto.getId());
        verify(postRepos, times(1)).deleteById(post.getId());
        assertEquals(postDto.getId(), postId);
    }
}