package com.springboot.rest.controller;

import com.springboot.rest.model.dto.*;
import com.springboot.rest.service.CommentService;
import com.springboot.rest.service.LikeService;
import com.springboot.rest.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@Slf4j
@RequestMapping("api/v1/resource")
@RolesAllowed("ROLE_USER")
public class ActivityController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public ActivityController(PostService postService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    /*
        Post related controls starts here
     */

    @PostMapping("post")
    @ApiOperation(value = "Add a post", notes = "User id should be provided in request payload",responseContainer = "ResponseEntity", response = ApiMessageResponse.class)

    public ResponseEntity<ApiMessageResponse> addUserPost(@RequestBody PostDto post) {
       return  ResponseEntity.ok().body(new ApiMessageResponse(postService.addPost(post)));
    }


    @PutMapping("post")
    @ApiOperation(value = "Update a post", responseContainer = "ResponseEntity", response = ApiMessageResponse.class)

    public ResponseEntity<ApiMessageResponse> updateUserPost(@RequestBody PostEditDto postEditDto) {
        return ResponseEntity.ok().body(new ApiMessageResponse(postService.updatePost(postEditDto)));
    }

    @GetMapping("profile/{userId}/posts/{pageNo}")
    @ApiOperation(value = "Get posts of specified user.",
            notes = "The path variables represent the id of user received on login and the page no for pagination", response = PostDto.class,
            responseContainer = "List")

    public ResponseEntity<DataList<PostDto>> getUserPosts(@PathVariable("userId") Long userId,
                                                          @PathVariable("pageNo") int pageNo) {

        DataList<PostDto> dataList = postService.getPostsOfUser(userId, pageNo);

        return ResponseEntity.ok().body(dataList);
    }

    @GetMapping("post/{postId}")
    @ApiOperation(value = "Get specified post of specified user",
            notes = "Post id should be specified as path variable", response = PostDto.class)
    public ResponseEntity<Data<PostDto>> getRequiredPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().body(postService.getSelectedPost(postId));
    }

    @GetMapping("posts/{pageNo}")
    @ApiOperation(value = "Get all the posts",
            notes = "Pagination provided for which path variable should contain page no", response = PostDto.class,
            responseContainer = "List")
    public ResponseEntity<DataList<PostDto>> getPostsForFeed(@PathVariable int pageNo) {
        postService.getPosts(pageNo);
        return ResponseEntity.ok().body(postService.getPosts(pageNo));
    }

    @DeleteMapping("post/{postId}")
    @ApiOperation(value = "Delete the specified post",
            notes = "Post id should be provided as path variable", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> deletePostOfUser(@PathVariable Long postId) {
        return ResponseEntity.ok().body(new ApiMessageResponse(postService.deletePost(postId)));
    }

    /*
        Post related controls ends here
     */



    /*
        Comment related controls starts here
     */

    @PostMapping("comment")
    @ApiOperation(value = "Add a comment to a post",
            notes = "Post id and User id should be provided as part of the request payload", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> addComment(@RequestBody CommentDto comment) {
        return ResponseEntity.ok().body(new ApiMessageResponse(commentService.addCommentOnActivity(comment)));
    }

    @PutMapping("comment")
    @ApiOperation(value = "Update comment on a post",
            notes = "Comment id is sufficient along with the required changes", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> updateComment(@RequestBody CommentEditDto commentEditDto) {
        return ResponseEntity.ok().body(new ApiMessageResponse(commentService.changeCommentOnActivity(commentEditDto)));
    }

    @ApiOperation(value = "Delete comment on post",
            notes = "Comment id is to be provided as a path variable", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ApiMessageResponse> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok().body(new ApiMessageResponse(commentService.delCommentOnActivity(commentId)));
    }

    @GetMapping("post/{postId}/comments/{pageNo}")
    @ApiOperation(value = "Get comments on post",
            notes = "post id and page no for pagination to be provided as path variables", response = CommentDto.class,
            responseContainer = "List")
    public ResponseEntity<DataList<CommentDto>> getCommentsOnPost(@PathVariable("postId") Long postId,
                                                                  @PathVariable("pageNo") int pageNo) {
        return ResponseEntity.ok().body(commentService.getCommentsOnPost(postId, pageNo));
    }

    @GetMapping("comment/{commentId}/replies/{pageNo}")
    @ApiOperation(value = "Get replies on a comment",
            notes = "Comment id and page no for pagination to be provided as path variables", response = CommentDto.class,
            responseContainer = "List")
    public ResponseEntity<DataList<CommentDto>> getRepliesOnComment(@PathVariable("commentId") Long commentId,
                                                 @PathVariable("pageNo") int pageNo) {
        return ResponseEntity.ok().body(commentService.getRepliesOnComment(commentId, pageNo));
    }


    /*
        Comment related controls ends here
     */



    /*
        Like related controls starts here
     */

    @PostMapping("post/{postId}/like")
    @ApiOperation(value = "Add like on a post",
            notes = "Post id and user id to be provided in request payload", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> addLikeOnPost(@RequestBody LikePostDto like) {
        return ResponseEntity.ok().body(new ApiMessageResponse(likeService.likeAPost(like)));
    }

    @PostMapping("comment/{commentId}/like")
    @ApiOperation(value = "Add like on a comment",
            notes = "Comment id and user id to be provided in request payload", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> addLikeOnComment(@RequestBody LikeCommentDto like) {
        return ResponseEntity.ok().body(new ApiMessageResponse(likeService.likeComment(like)));
    }

    @DeleteMapping("post/{postId}/unlike")
    @ApiOperation(value = "Delete like on a post",
            notes = "Post id and user id to be provided in request payload", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> removeLikeOnPost(@RequestBody LikePostDto like) {
        return ResponseEntity.ok().body(new ApiMessageResponse(likeService.unlikePost(like)));
    }

    @DeleteMapping("comment/{commentId}/unlike")
    @ApiOperation(value = "Delete like on a comment",
            notes = "Comment id and user id to be provided in request payload", response = ApiMessageResponse.class,
            responseContainer = "ResponseEntity")
    public ResponseEntity<ApiMessageResponse> removeLikeOnComment(@RequestBody LikeCommentDto like) {
        return ResponseEntity.ok().body(new ApiMessageResponse(likeService.unlikeComment(like)));
    }

    @ApiOperation(value = "Get likes on a post",
            notes = "Post id to be provided as path variable", response = LikePostDto.class,
            responseContainer = "List")
    @GetMapping("post/{postId}/likes")
    public ResponseEntity<DataList<LikePostDto>> getLikesOnPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().body(likeService.getLikesOnPost(postId));
    }

    @GetMapping("comment/{commentId}/likes")
    @ApiOperation(value = "Get likes on a comment",
            notes = "Comment id to be provided as path variable", response = LikeCommentDto.class,
            responseContainer = "List")
    public ResponseEntity<DataList<LikeCommentDto>> getLikesOnComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok().body(likeService.getLikesOnComment(commentId));
    }



    /*
        Like related controls ends here
     */


}
