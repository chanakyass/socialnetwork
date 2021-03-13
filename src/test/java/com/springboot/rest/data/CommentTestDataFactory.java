package com.springboot.rest.data;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentTestDataFactory {
    private final CommentService commentService;
    private final HashMap<String, Object> resourcesHashMap;
    private final UserTestDataFactory userTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final List<CommentDto> commentsInDb;

    @Autowired
    public CommentTestDataFactory(CommentService commentService, UserTestDataFactory userTestDataFactory, PostTestDataFactory postTestDataFactory,
                                  @Qualifier("testExistingResources") HashMap<String, Object> resourcesHashMap,
                                    @Qualifier("testCommentsListForRead") List<CommentDto> commentsInDb)
    {
        this.commentService = commentService;
        this.postTestDataFactory = postTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
        this.resourcesHashMap = resourcesHashMap;
        this.commentsInDb = commentsInDb;
    }

    public CommentDto createNewComment()
    {
        CommentDto latest = new CommentDto();
        latest.setCommentContent("This is a comment");
        latest.setCommentedAtTime(LocalDateTime.now());
        return latest;
    }

    public CommentDto createCommentTemplateForLoggedInUser()
    {
        CommentDto comment =  createNewComment();
        UserDto user = userTestDataFactory.getLoggedInUser();
        PostDto post = postTestDataFactory.getPreExistingPost();
        comment.setOwner(user);
        comment.setCommentedOn(post);
        return comment;
    }

    public CommentDto createCommentTemplateForOtherUser()
    {
        CommentDto comment =  createNewComment();
        UserDto user = userTestDataFactory.getOtherThanLoggedInUser();
        PostDto post = postTestDataFactory.getPreExistingPost();
        comment.setCommentedOn(post);
        comment.setOwner(user);
        return comment;
    }

    public Long createCommentTemplateForLoggedInUserAndInsertInDb()
    {
        CommentDto commentDto = createCommentTemplateForLoggedInUser();
        ResponseEntity<ApiMessageResponse> message =  commentService.addCommentOnActivity(commentDto);
        return Objects.requireNonNull(message.getBody()).getResourceId();
    }


    public CommentDto getPreExistingComment()
    {
        return (CommentDto) resourcesHashMap.get("EXISTING_COMMENT");
    }

    public List<CommentDto> getAllCommentsOnPost(PostDto reqPost)
    {
        return commentsInDb.stream().filter((CommentDto comment)->(comment.getCommentedOn()!=null && comment.getCommentedOn().getId().compareTo(reqPost.getId()) == 0)
                && (comment.getParentComment() == null)).collect(Collectors.toList());
    }

    public List<CommentDto> getRepliesOnComment(CommentDto reqComment)
    {
        return commentsInDb.stream().filter((CommentDto comment)->comment.getParentComment()!= null
                            && comment.getParentComment().getId().compareTo(reqComment.getId()) == 0).collect(Collectors.toList());
    }
}
