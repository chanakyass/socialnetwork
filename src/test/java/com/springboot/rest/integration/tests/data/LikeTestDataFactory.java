package com.springboot.rest.integration.tests.data;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.post.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeTestDataFactory {
    private final UserTestDataFactory userTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final CommentTestDataFactory commentTestDataFactory;
    private final HashMap<String, Object> resourceHashMap;
    private final List<LikePostDto> likesOnPostsInDb;
    private final List<LikeCommentDto> likesOnCommentsInDb;

    @Autowired
    public LikeTestDataFactory(UserTestDataFactory userTestDataFactory,
                               PostTestDataFactory postTestDataFactory,
                               CommentTestDataFactory commentTestDataFactory,
                               @Qualifier("testExistingResources") HashMap<String, Object> resourceHashMap,
                               @Qualifier("testLikePostsListForRead") List<LikePostDto> likesOnPostsInDb,
                               @Qualifier("testLikeCommentsListForRead")List<LikeCommentDto> likesOnCommentsInDb)
    {
        this.userTestDataFactory = userTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
        this.commentTestDataFactory = commentTestDataFactory;
        this.resourceHashMap = resourceHashMap;
        this.likesOnPostsInDb = likesOnPostsInDb;
        this.likesOnCommentsInDb = likesOnCommentsInDb;
    }

    public LikePostDto createLikeOnPostForLoggedInUser()
    {
        LikePostDto like = new LikePostDto();
        like.setOwner(userTestDataFactory.getLoggedInUser());
        like.setLikedPost(postTestDataFactory.getPreExistingPost());
        return like;
    }

    public LikePostDto createLikeOnPostForOtherUser()
    {
        LikePostDto like = new LikePostDto();
        like.setOwner(userTestDataFactory.getOtherThanLoggedInUser());
        like.setLikedPost(postTestDataFactory.getPreExistingPost());
        return like;
    }

    public LikeCommentDto createLikeOnCommentForLoggedInUser()
    {
        LikeCommentDto like = new LikeCommentDto();
        like.setOwner(userTestDataFactory.getLoggedInUser());
        like.setLikedComment(commentTestDataFactory.getPreExistingComment());
        return like;
    }

    public LikeCommentDto createLikeOnCommentForOtherUser()
    {
        LikeCommentDto like = new LikeCommentDto();
        like.setOwner(userTestDataFactory.getOtherThanLoggedInUser());
        like.setLikedComment(commentTestDataFactory.getPreExistingComment());
        return like;
    }

    public LikePostDto getPreExistingLikePost()
    {
        return (LikePostDto) resourceHashMap.get("EXISTING_LIKE_POST");
    }

    public LikeCommentDto getPreExistingLikeComment()
    {
        return (LikeCommentDto) resourceHashMap.get("EXISTING_LIKE_COMMENT");
    }

    public List<LikePostDto> getLikesOnPost(PostDto postDto)
    {
        return likesOnPostsInDb.stream().filter((LikePostDto like)->like.getId()!=null && like.getLikedPost()!=null
                && like.getLikedPost().getId().compareTo(postDto.getId()) == 0).collect(Collectors.toList());
    }

    public List<LikeCommentDto> getLikesOnComment(CommentDto commentDto)
    {
        return likesOnCommentsInDb.stream().filter((LikeCommentDto like)->like.getId()!=null && like.getLikedComment()!=null
                && like.getLikedComment().getId().compareTo(commentDto.getId()) == 0).collect(Collectors.toList());
    }

/*    public PostDto createPostForLoggedInUserAndAddToDB()
    {
        PostDto post = createPostForLoggedInUser();
        ResponseEntity<ApiMessageResponse> message = postService.addPost(post);
        Long postId = Objects.requireNonNull(message.getBody()).getResourceId();
        return postService.getSelectedPost(postId);
    }*/


}
