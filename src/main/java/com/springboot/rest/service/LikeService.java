package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiResourceNotFoundException;
import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.LikeComment;
import com.springboot.rest.model.entities.LikePost;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.LikeCommentMapper;
import com.springboot.rest.model.mapper.LikePostMapper;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.LikeCommentRepos;
import com.springboot.rest.repository.LikePostRepos;
import com.springboot.rest.repository.PostRepos;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LikePostMapper likePostMapper;
    private final LikeCommentMapper likeCommentMapper;
    private final SecurityUtils securityUtils;

    @Autowired
    public LikeService(LikePostRepos likePostRepos, LikeCommentRepos likeCommentRepos,
                       PostRepos postRepos, CommentRepos commentRepos,
                        LikePostMapper likePostMapper, LikeCommentMapper likeCommentMapper, SecurityUtils securityUtils) {
        this.likePostRepos = likePostRepos;
        this.likeCommentRepos = likeCommentRepos;
        this.postRepos = postRepos;
        this.commentRepos = commentRepos;
        this.likePostMapper = likePostMapper;
        this.likeCommentMapper = likeCommentMapper;
        this.securityUtils = securityUtils;
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likePostDto, \"CREATE\")")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'L')")
    public Long likeAPost(LikePostDto likePostDto) {

        LikePost like = likePostMapper.toLikePost(likePostDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiResourceNotFoundException("Post doesn't exist"));

        LikePost likePost = likePostRepos.findLikeByLikedPost_IdAndOwner_Id(likePostDto.getLikedPost().getId(), likePostDto.getOwnerId()).orElse(null);

        if(likePost!=null)
        {
            throw new ApiSpecificException("Post cannot be liked more than once");
        }

        //post.setNoOfLikes(post.getNoOfLikes() + 1);
        LikePost responseLikePost = likePostRepos.save(like);
        return responseLikePost.getLikedPost().getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likeCommentDto, \"CREATE\")")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'Q')")
    public Long likeComment(LikeCommentDto likeCommentDto) {

        LikeComment like = likeCommentMapper.toLikeComment(likeCommentDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiResourceNotFoundException("Comment doesn't exist"));

        LikeComment likeComment = likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(like.getLikedComment().getId(),
                like.getOwner().getId()).orElse(null);
        if (likeComment != null) {
            throw new ApiSpecificException("Comment cannot be liked more than once");
        }

        //comment.setNoOfLikes(comment.getNoOfLikes() + 1);
        LikeComment responseLikeComment = likeCommentRepos.save(like);
        return responseLikeComment.getLikedComment().getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likePostDto, \"DELETE\")")
    @PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject, 'L')")
    public Long unlikePost(LikePostDto likePostDto) {

        LikePost like = likePostMapper.toLikePost(likePostDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiResourceNotFoundException("Post doesn't exist"));
        //post.setNoOfLikes(post.getNoOfLikes() - 1);
        LikePost likePost = likePostRepos.findLikeByLikedPost_IdAndOwner_Id(like.getLikedPost().getId(), like.getOwner().getId()).orElseThrow(() ->
                new ApiSpecificException("Nothing to unlike"));

        likePostRepos.deleteById(likePost.getId());
        return likePost.getLikedPost().getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likeCommentDto, \"DELETE\")")
    @PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject, 'Q')")
    public Long unlikeComment(LikeCommentDto likeCommentDto) {

        LikeComment like = likeCommentMapper.toLikeComment(likeCommentDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));

        LikeComment likeComment = likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(like.getLikedComment().getId(),
                like.getOwner().getId()).orElseThrow(() -> new ApiSpecificException("Nothing to unlike"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiResourceNotFoundException("Comment doesn't exist"));
        //comment.setNoOfLikes(comment.getNoOfLikes() - 1);
        likeCommentRepos.deleteById(likeComment.getId());
        return likeComment.getLikedComment().getId();

    }

    public DataList<LikePostDto> getLikesOnPost(long postId) {
        postRepos.findById(postId).orElseThrow(() -> new ApiResourceNotFoundException("Post doesn't exist"));
        List<LikePost> likes =  likePostRepos.findLikesByLikedPost_Id(postId).orElseThrow(() -> new ApiSpecificException("No likes on post"));
        return new DataList<>(likePostMapper.toLikePostDtoList(likes));
    }

    public DataList<LikeCommentDto> getLikesOnComment(long commentId) {
        commentRepos.findById(commentId).orElseThrow(() -> new ApiResourceNotFoundException("Comment doesn't exist"));
        List<LikeComment> likes = likeCommentRepos.findLikesByLikedComment_Id(commentId).orElseThrow(() -> new ApiSpecificException("No likes on comment"));
        return new DataList<>(likeCommentMapper.toLikeCommentDtoList(likes));
    }

    public boolean hasLoggedInUserLikedPost(Long postId){
         return likePostRepos.findLikeByLikedPost_IdAndOwner_Id(postId, securityUtils.getSubjectId()).isPresent();
    }

    public boolean hasLoggedInUserLikedComment(Long commentId){
        return likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(commentId, securityUtils.getSubjectId()).isPresent();
    }


}
