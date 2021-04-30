package com.springboot.rest.controller;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.dto.response.ApiMessageResponse;
import com.springboot.rest.model.dto.response.Data;
import com.springboot.rest.model.dto.response.DataAndList;
import com.springboot.rest.model.dto.response.DataList;
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
    @ApiOperation(value = "Add a post", notes = "User id should be provided in request payload", response = ApiMessageResponse.class)

    public ResponseEntity<Data<ApiMessageResponse>> addUserPost(@RequestBody PostDto post) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(postService.addPost(post));
       return  ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }


    @PutMapping("post")
    @ApiOperation(value = "Update a post", response = ApiMessageResponse.class)

    public ResponseEntity<Data<ApiMessageResponse>> updateUserPost(@RequestBody PostEditDto postEditDto) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(postService.updatePost(postEditDto));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @GetMapping("profile/{userId}/posts")
    @ApiOperation(value = "Get posts of specified user.",
            notes = "The path variables represent the id of user received on login and the page no for pagination", response = PostDto.class,
            responseContainer = "DataList")

    public ResponseEntity<DataList<PostDto>> getUserPosts(@PathVariable("userId") Long userId,
                                                          @RequestParam(name="pageNo") int pageNo,
                                                          @RequestParam(name="adjustments") int noOfDeletions) {

        DataList<PostDto> dataList = postService.getPostsOfUser(userId, pageNo, noOfDeletions);

        return ResponseEntity.ok().body(dataList);
    }

    @GetMapping("post/{postId}")
    @ApiOperation(value = "Get specified post of specified user",
            notes = "Post id should be specified as path variable", response = PostDto.class, responseContainer = "Data")
    public ResponseEntity<Data<PostDto>> getRequiredPost(@PathVariable("postId") Long postId) {
        PostDto postDto = postService.getSelectedPost(postId);
        return ResponseEntity.ok().body(new Data<>(postDto));
    }

    @GetMapping("posts")
    @ApiOperation(value = "Get all the posts",
            notes = "Pagination provided for which path variable should contain pageDetailsDto no", response = PostDto.class,
            responseContainer = "DataList")
    public ResponseEntity<DataList<PostDto>> getPostsForFeed(@RequestParam(name="pageNo") int pageNo, @RequestParam(name="adjustments")int deletions) {
        return ResponseEntity.ok().body(postService.getPosts(pageNo, deletions));
    }

    @DeleteMapping("post/{postId}")
    @ApiOperation(value = "Delete the specified post",
            notes = "Post id should be provided as path variable", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> deletePostOfUser(@PathVariable Long postId) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(postService.deletePost(postId));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @GetMapping("post&comments/{postId}/comments")
    @ApiOperation(value = "Get the specified post along with comments",
            notes = "Post id should be provided as path variable along with page no for comments", response = ApiMessageResponse.class)
    public ResponseEntity<DataAndList<PostDto, CommentDto>> getSelectedPostAndComments(@PathVariable Long postId,
                                                                                       @RequestParam(name="pageNo") int pageNo,
                                                                                       @RequestParam(name="adjustments") int noOfDeletions)
    {
        PostDto selectedPost = postService.getSelectedPost(postId);
        Data<PostDto> data = new Data<>(selectedPost);
        DataList<CommentDto> comments = commentService.getCommentsOnPost(postId, pageNo, noOfDeletions);
        return ResponseEntity.ok().body(new DataAndList<>(data, comments));
    }

    /*
        Post related controls ends here
     */



    /*
        Comment related controls starts here
     */

    @PostMapping("comment")
    @ApiOperation(value = "Add a comment to a post",
            notes = "Post id and User id should be provided as part of the request payload", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> addComment(@RequestBody CommentDto comment) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(commentService.addCommentOnActivity(comment));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @PutMapping("comment")
    @ApiOperation(value = "Update comment on a post",
            notes = "Comment id is sufficient along with the required changes", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> updateComment(@RequestBody CommentEditDto commentEditDto) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(commentService.changeCommentOnActivity(commentEditDto));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @ApiOperation(value = "Delete comment on post",
            notes = "Comment id is to be provided as a path variable", response = ApiMessageResponse.class)
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<Data<ApiMessageResponse>> deleteComment(@PathVariable Long commentId) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(commentService.delCommentOnActivity(commentId));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @GetMapping("post/{postId}/comments")
    @ApiOperation(value = "Get comments on post",
            notes = "post id and page no for pagination to be provided as path variables", response = CommentDto.class,
            responseContainer = "DataList")
    public ResponseEntity<DataList<CommentDto>> getCommentsOnPost(@PathVariable("postId") Long postId,
                                                                  @RequestParam(name="pageNo") int pageNo,
                                                                  @RequestParam(name="adjustments") int noOfDeletions) {
        return ResponseEntity.ok().body(commentService.getCommentsOnPost(postId, pageNo, noOfDeletions));
    }

    @GetMapping("comment/{commentId}/replies")
    @ApiOperation(value = "Get replies on a comment",
            notes = "Comment id and page no for pagination to be provided as path variables", response = CommentDto.class,
            responseContainer = "DataList")
    public ResponseEntity<DataList<CommentDto>> getRepliesOnComment(@PathVariable("commentId") Long commentId,
                                                 @RequestParam(name="pageNo") int pageNo,
                                                 @RequestParam(name="adjustments") int noOfDeletions) {
        return ResponseEntity.ok().body(commentService.getRepliesOnComment(commentId, pageNo, noOfDeletions));
    }


    /*
        Comment related controls ends here
     */



    /*
        Like related controls starts here
     */

    @PostMapping("post/{postId}/like")
    @ApiOperation(value = "Add like on a post",
            notes = "Post id and user id to be provided in request payload", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> addLikeOnPost(@RequestBody LikePostDto like) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(likeService.likeAPost(like));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @PostMapping("comment/{commentId}/like")
    @ApiOperation(value = "Add like on a comment",
            notes = "Comment id and user id to be provided in request payload", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> addLikeOnComment(@RequestBody LikeCommentDto like) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(likeService.likeComment(like));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @DeleteMapping("post/{postId}/unlike")
    @ApiOperation(value = "Delete like on a post",
            notes = "Post id and user id to be provided in request payload", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> removeLikeOnPost(@RequestBody LikePostDto like) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(likeService.unlikePost(like));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @DeleteMapping("comment/{commentId}/unlike")
    @ApiOperation(value = "Delete like on a comment",
            notes = "Comment id and user id to be provided in request payload", response = ApiMessageResponse.class)
    public ResponseEntity<Data<ApiMessageResponse>> removeLikeOnComment(@RequestBody LikeCommentDto like) {
        ApiMessageResponse apiMessageResponse = new ApiMessageResponse(likeService.unlikeComment(like));
        return ResponseEntity.ok().body(new Data<>(apiMessageResponse));
    }

    @ApiOperation(value = "Get likes on a post",
            notes = "Post id to be provided as path variable", response = LikePostDto.class,
            responseContainer = "DataList")
    @GetMapping("post/{postId}/likes")
    public ResponseEntity<DataList<LikePostDto>> getLikesOnPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().body(likeService.getLikesOnPost(postId));
    }

    @GetMapping("comment/{commentId}/likes")
    @ApiOperation(value = "Get likes on a comment",
            notes = "Comment id to be provided as path variable", response = LikeCommentDto.class,
            responseContainer = "DataList")
    public ResponseEntity<DataList<LikeCommentDto>> getLikesOnComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok().body(likeService.getLikesOnComment(commentId));
    }



    /*
        Like related controls ends here
     */


}
