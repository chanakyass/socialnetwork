package com.springboot.rest.data;

import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentTestDataFactory {
    private final CommentService commentService;
    private final HashMap<String, Object> resourcesHashMap;
    private final UserTestDataFactory userTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final List<Comment> commentsInDb;

    @Autowired
    public CommentTestDataFactory(CommentService commentService, UserTestDataFactory userTestDataFactory, PostTestDataFactory postTestDataFactory,
                                  @Qualifier("testExistingResources") HashMap<String, Object> resourcesHashMap,
                                    @Qualifier("testCommentsListForRead") List<Comment> commentsInDb)
    {
        this.commentService = commentService;
        this.postTestDataFactory = postTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
        this.resourcesHashMap = resourcesHashMap;
        this.commentsInDb = commentsInDb;
    }

    public Comment createNewComment()
    {
        Comment latest = new Comment();
        latest.setCommentContent("This is a comment");
        latest.setNoOfLikes(0L);
        latest.setCommentedOnDate(LocalDate.of(2020,5,10));
        return latest;
    }

    public Comment createCommentTemplateForLoggedInUser()
    {
        Comment comment =  createNewComment();
        User user = userTestDataFactory.getLoggedInUser();
        Post post = postTestDataFactory.getPreExistingPost();
        comment.setOwner(user);
        comment.setCommentedOn(post);
        return comment;
    }

    public Comment createCommentTemplateForOtherUser()
    {
        Comment comment =  createNewComment();
        User user = userTestDataFactory.getOtherThanLoggedInUser();
        Post post = postTestDataFactory.getPreExistingPost();
        comment.setCommentedOn(post);
        comment.setOwner(user);
        return comment;
    }


    public Comment getPreExistingComment()
    {
        return (Comment) resourcesHashMap.get("EXISTING_COMMENT");
    }

    public List<Comment> getAllCommentsOnPost(Post reqPost)
    {
        return commentsInDb.stream().filter((Comment comment)->(comment.getCommentedOn()!=null && comment.getCommentedOn().getId().compareTo(reqPost.getId()) == 0)
                && (comment.getParentComment() == null)).collect(Collectors.toList());
    }

    public List<Comment> getRepliesOnComment(Comment reqComment)
    {
        return commentsInDb.stream().filter((Comment comment)->comment.getParentComment()!= null
                            && comment.getParentComment().getId().compareTo(reqComment.getId()) == 0).collect(Collectors.toList());
    }
}
