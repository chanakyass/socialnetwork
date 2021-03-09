package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.LikeComment;
import com.springboot.rest.model.entities.LikePost;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.LikeCommentView;
import com.springboot.rest.model.projections.LikePostView;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.LikeCommentRepos;
import com.springboot.rest.repository.LikePostRepos;
import com.springboot.rest.repository.PostRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikePostRepos likePostRepos;
    private final LikeCommentRepos likeCommentRepos;
    private final PostRepos postRepos;
    private final CommentRepos commentRepos;

    @Autowired
    public LikeService(LikePostRepos likePostRepos, LikeCommentRepos likeCommentRepos,
                       PostRepos postRepos, CommentRepos commentRepos) {
        this.likePostRepos = likePostRepos;
        this.likeCommentRepos = likeCommentRepos;
        this.postRepos = postRepos;
        this.commentRepos = commentRepos;
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#like, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject.body.resourceId, 'L')")
    public ResponseEntity<ApiMessageResponse> likeAPost(LikePost like) {
        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));
        post.setNoOfLikes(post.getNoOfLikes() + 1);
        LikePost responseLikePost = likePostRepos.save(like);
        return ResponseEntity.ok().body(new ApiMessageResponse(responseLikePost.getId()));
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#like, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject.body.resourceId, 'Q')")
    public ResponseEntity<ApiMessageResponse> likeComment(LikeComment like) {
        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));
        comment.setNoOfLikes(comment.getNoOfLikes() + 1);
        LikeComment responseLikeComment = likeCommentRepos.save(like);
        return ResponseEntity.ok().body(new ApiMessageResponse(responseLikeComment.getId()));
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#like, null)")
    public ResponseEntity<ApiMessageResponse> unlikePost(LikePost like) {
        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));
        post.setNoOfLikes(post.getNoOfLikes() - 1);
        LikePostView likePostProjection = likePostRepos.findLikeByLikedPost_IdAndOwner_Id(like.getLikedPost().getId(), like.getOwner().getId()).orElseThrow(() ->
                new ApiSpecificException("Nothing to unlike"));

        likePostRepos.deleteById(likePostProjection.getId());
        return ResponseEntity.ok().body(new ApiMessageResponse(like.getId()));
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#like, null)")
    public ResponseEntity<ApiMessageResponse> unlikeComment(LikeComment like) {
        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));

        likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(like.getLikedComment().getId(),
                like.getOwner().getId()).orElseThrow(() -> new ApiSpecificException("Nothing to unlike"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));
        comment.setNoOfLikes(comment.getNoOfLikes() - 1);
        likeCommentRepos.deleteById(like.getId());
        return ResponseEntity.ok().body(new ApiMessageResponse(like.getId()));

    }

    public List<LikePostView> getLikesOnPost(long postId) {
        return likePostRepos.findLikesByLikedPost_Id(postId).orElseThrow(() -> new ApiSpecificException("No likes on post"));
    }

    public List<LikeCommentView> getLikesOnComment(long commentId) {
        return likeCommentRepos.findLikesByLikedComment_Id(commentId).orElseThrow(() -> new ApiSpecificException("No likes on comment"));
    }


}
