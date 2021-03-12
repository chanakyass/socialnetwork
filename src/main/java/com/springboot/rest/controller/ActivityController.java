package com.springboot.rest.controller;

import com.springboot.rest.model.dto.*;
import com.springboot.rest.service.AuthorizationService;
import com.springboot.rest.service.CommentService;
import com.springboot.rest.service.LikeService;
import com.springboot.rest.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/resource")
@RolesAllowed("ROLE_USER")
public class ActivityController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final AuthorizationService authorizationService;

    @Autowired

    public ActivityController(PostService postService, CommentService commentService, LikeService likeService, AuthorizationService authorizationService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.authorizationService = authorizationService;
    }

    /*
        Post related controls starts here
     */

    @GetMapping("posts/{pageNo}")
    public List<PostDto> getPostsForFeed(@PathVariable int pageNo) {
        return postService.getPosts(pageNo);
    }

    @GetMapping("profile/{userId}/posts/{pageNo}")
    public List<PostDto> getPostsOfUser(@PathVariable("userId") Long user_id,
                                         @PathVariable("pageNo") int pageNo) {
        return postService.getPostsOfUser(user_id, pageNo);
    }

    @GetMapping("post/{postId}")
    public PostDto getPostReq(@PathVariable("postId") Long postId) {
        return postService.getSelectedPost(postId);
    }

    @PostMapping("post")
    public ResponseEntity<ApiMessageResponse> addPostOfUser(@RequestBody PostDto post) {
        return postService.addPost(post);
    }

    @PutMapping("post")
    public ResponseEntity<ApiMessageResponse> updatePostOfUser(@RequestBody PostEditDto postEditDto) {
        return postService.updatePost(postEditDto);
    }

    @DeleteMapping("post/{postId}")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> deletePostOfUser(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    /*
        Post related controls ends here
     */



    /*
        Comment related controls starts here
     */

    @PostMapping("comment")
    public ResponseEntity<ApiMessageResponse> addComment(@RequestBody CommentDto comment) {
        return commentService.addCommentOnActivity(comment);
    }

    @PutMapping("comment")
    public ResponseEntity<ApiMessageResponse> updateComment(@RequestBody CommentEditDto commentEditDto) {
        return commentService.changeCommentOnActivity(commentEditDto);
    }

    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ApiMessageResponse> deleteComment(@PathVariable Long commentId) {
        return commentService.delCommentOnActivity(commentId);
    }

    @GetMapping("post/{postId}/comments/{pageNo}")
    public List<CommentDto> getCommentsOnPost(@PathVariable("postId") Long postId,
                                               @PathVariable("pageNo") int pageNo) {
        return commentService.getCommentsOnPost(postId, pageNo);
    }

    @GetMapping("comment/{commentId}/replies/{pageNo}")
    public List<CommentDto> getRepliesOnComment(@PathVariable("commentId") Long commentId,
                                                 @PathVariable("pageNo") int pageNo) {
        List<CommentDto> comments =  commentService.getRepliesOnComment(commentId, pageNo);
        System.out.println("to debug");
        return comments;
    }


    /*
        Comment related controls ends here
     */



    /*
        Like related controls starts here
     */

    @PostMapping("post/{postId}/like")
    public ResponseEntity<ApiMessageResponse> addLikeOnPost(@RequestBody LikePostDto like) {
        return likeService.likeAPost(like);
    }

    @PostMapping("comment/{commentId}/like")
    public ResponseEntity<ApiMessageResponse> addLikeOnComment(@RequestBody LikeCommentDto like) {
        return likeService.likeComment(like);
    }

    @DeleteMapping("post/{postId}/unlike")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> removeLikeOnPost(@RequestBody LikePostDto like) {
        return likeService.unlikePost(like);
    }

    @DeleteMapping("comment/{commentId}/unlike")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> removeLikeOnComment(@RequestBody LikeCommentDto like) {
        return likeService.unlikeComment(like);
    }

    @GetMapping("post/{postId}/likes")
    public List<LikePostDto> getLikesOnPost(@PathVariable("postId") Long postId) {
        return likeService.getLikesOnPost(postId);
    }

    @GetMapping("comment/{commentId}/likes")
    public List<LikeCommentDto> getLikesOnComment(@PathVariable("commentId") Long commentId) {
        return likeService.getLikesOnComment(commentId);
    }



    /*
        Like related controls ends here
     */


}
