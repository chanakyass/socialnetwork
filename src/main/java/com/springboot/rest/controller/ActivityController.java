package com.springboot.rest.controller;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.CommentEdit;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.LikeComment;
import com.springboot.rest.model.entities.LikePost;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.CommentView;
import com.springboot.rest.model.projections.LikeCommentView;
import com.springboot.rest.model.projections.LikePostView;
import com.springboot.rest.model.projections.PostView;
import com.springboot.rest.service.AuthorizationService;
import com.springboot.rest.service.CommentService;
import com.springboot.rest.service.LikeService;
import com.springboot.rest.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<PostView> getPostsForFeed(@PathVariable int pageNo) {
        return postService.getPosts(pageNo);
    }

    @GetMapping("profile/{userId}/posts/{pageNo}")
    public List<PostView> getPostsOfUser(@PathVariable("userId") Long user_id,
                                         @PathVariable("pageNo") int pageNo) {
        return postService.getPostsOfUser(user_id, pageNo);
    }

    @GetMapping("post/{postId}")
    public PostView getPostReq(@PathVariable("postId") Long postId) {
        return postService.getSelectedPost(postId);
    }

    @PostMapping("post")
    public ResponseEntity<ApiMessageResponse> addPostOfUser(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @PutMapping("post")
    public ResponseEntity<ApiMessageResponse> updatePostOfUser(@RequestBody PostDto postDto) {
        return postService.updatePost(postDto);
    }

    @DeleteMapping("post")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> deletePostOfUser(@RequestBody Post post) {
        return postService.deletePost(post);
    }

    /*
        Post related controls ends here
     */



    /*
        Comment related controls starts here
     */

    @PostMapping("comment")
    public ResponseEntity<ApiMessageResponse> addComment(@RequestBody Comment comment) {
        return commentService.addCommentOnActivity(comment);
    }

    @PutMapping("comment")
    public ResponseEntity<ApiMessageResponse> updateComment(@RequestBody CommentEdit commentEdit) {
        return commentService.changeCommentOnActivity(commentEdit);
    }

    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    @DeleteMapping("comment")
    public ResponseEntity<ApiMessageResponse> deleteComment(@RequestBody Comment comment) {
        return commentService.delCommentOnActivity(comment);
    }

    @GetMapping("post/{postId}/comments/{pageNo}")
    public List<CommentView> getCommentsOnPost(@PathVariable("postId") Long postId,
                                               @PathVariable("pageNo") int pageNo) {
        return commentService.getCommentsOnPost(postId, pageNo);
    }

    @GetMapping("comment/{commentId}/replies/{pageNo}")
    public List<CommentView> getRepliesOnComment(@PathVariable("commentId") Long commentId,
                                                 @PathVariable("pageNo") int pageNo) {
        return commentService.getRepliesOnComment(commentId, pageNo);
    }


    /*
        Comment related controls ends here
     */



    /*
        Like related controls starts here
     */

    @PostMapping("post/{postId}/like")
    public ResponseEntity<ApiMessageResponse> addLikeOnPost(@RequestBody LikePost like) {
        return likeService.likeAPost(like);
    }

    @PostMapping("comment/{commentId}/like")
    public ResponseEntity<ApiMessageResponse> addLikeOnComment(@RequestBody LikeComment like) {
        return likeService.likeComment(like);
    }

    @PostMapping("post/{postId}/unlike")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> removeLikeOnPost(@RequestBody LikePost like) {
        return likeService.unlikePost(like);
    }

    @PostMapping("comment/{commentId}/unlike")
    //@PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject.body.resourceId)")
    public ResponseEntity<ApiMessageResponse> removeLikeOnComment(@RequestBody LikeComment like) {
        return likeService.unlikeComment(like);
    }

    @GetMapping("post/{postId}/likes")
    public List<LikePostView> getLikesOnPost(@PathVariable("postId") Long postId) {
        return likeService.getLikesOnPost(postId);
    }

    @GetMapping("comment/{commentId}/likes")
    public List<LikeCommentView> getLikesOnComment(@PathVariable("commentId") Long commentId) {
        return likeService.getLikesOnComment(commentId);
    }



    /*
        Like related controls ends here
     */


}
