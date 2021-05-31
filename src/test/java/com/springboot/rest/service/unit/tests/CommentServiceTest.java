package com.springboot.rest.service.unit.tests;

import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.mapper.CommentEditMapper;
import com.springboot.rest.model.mapper.CommentMapper;
import com.springboot.rest.model.projections.CommentView;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.service.CommentService;
import com.springboot.rest.service.unit.data.AbstractDataFactory;
import com.springboot.rest.service.unit.data.CommentTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepos postRepos;
    @Mock
    private CommentRepos commentRepos;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentEditMapper commentEditMapper;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentDto commentDto;
    private CommentEditDto commentEditDto;
    private List<CommentView> commentViewList;
    private List<CommentDto> commentDtoList;

    @BeforeEach
    void setUp() {

        CommentTestDataFactory commentTestDataFactory = AbstractDataFactory.provideCommentTestDataFactory();

        comment = commentTestDataFactory.getRandomComment();

        commentDto = commentTestDataFactory.getRandomCommentDto();

        commentEditDto = commentTestDataFactory.getRandomCommentEditDto();

        commentViewList = commentTestDataFactory.getListOfCommentViews();

        commentDtoList = commentTestDataFactory.getRandomListOfCommentDtos();
    }

    @Test
    void addCommentOnActivity() {
        when(postRepos.findById(1L)).thenReturn(Optional.ofNullable(
                comment.getCommentedOn()));
        when(commentRepos.save(any())).thenReturn(comment);
        when(commentMapper.toComment(commentDto)).thenReturn(comment);

        Long commentId = commentService.addCommentOnActivity(commentDto);

        verify(commentRepos, times(1)).save(any(Comment.class));

        assertEquals(comment.getId(), commentId);
    }

    @Test
    void getCommentsOnPost() {
        @SuppressWarnings("unchecked")
        Class<Page<CommentView>> classType = (Class<Page<CommentView>>)(Object)Page.class;
        Page<CommentView> page = Mockito.mock(classType);
        assert page!=null;
        when(commentRepos.findLevelOneCommentsOnPost(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(Optional.of(page));
        when(page.getContent()).thenReturn(commentViewList);
        when(page.getTotalPages()).thenReturn(commentViewList.size());
        when(commentMapper.toCommentDtoListFromView(commentViewList)).thenReturn(commentDtoList);

        DataList<CommentDto> dataList = commentService.getCommentsOnPost(1L, 0, 0);

        verify(commentRepos, times(1)).findLevelOneCommentsOnPost(anyLong(), anyLong(), any(Pageable.class));

        assertEquals(commentDtoList, dataList.getDataList());
    }

    @Test
    void getRepliesOnComment() {
        @SuppressWarnings("unchecked")
        Class<Page<CommentView>> classType = (Class<Page<CommentView>>)(Object)Page.class;
        Page<CommentView> page = Mockito.mock(classType);
        assert page!=null;
        when(commentRepos.findCommentsWithParentCommentAs(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(Optional.of(page));
        when(page.getContent()).thenReturn(commentViewList);
        when(page.getTotalPages()).thenReturn(commentViewList.size());
        when(commentMapper.toCommentDtoListFromView(commentViewList)).thenReturn(commentDtoList);
        when(commentRepos.findById(anyLong())).thenReturn(Optional.ofNullable(comment));

        DataList<CommentDto> dataList = commentService.getRepliesOnComment(1L, 0, 0);

        verify(commentRepos, times(1)).findCommentsWithParentCommentAs(anyLong(), anyLong(), any(Pageable.class));

        assertEquals(commentDtoList, dataList.getDataList());
    }

    @Test
    void delCommentOnActivity() {
        when(commentRepos.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        Long commentId = commentService.delCommentOnActivity(anyLong());
        verify(commentRepos, times(1)).deleteById(anyLong());
        assertEquals(comment.getId(), commentId);
    }

    @Test
    void changeCommentOnActivity() {
        when(commentRepos.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        doAnswer(invocation -> {
            Object [] args = invocation.getArguments();
            CommentTestDataFactory.editComment((CommentEditDto) args[0], (Comment) args[1]);
            return null;
        }).when(commentEditMapper).update(commentEditDto, comment);
        assertEquals(comment.getId(), commentService.changeCommentOnActivity(commentEditDto));
    }
}