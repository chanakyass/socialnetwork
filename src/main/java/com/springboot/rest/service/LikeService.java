package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.DataList;
import com.springboot.rest.model.dto.LikeCommentDto;
import com.springboot.rest.model.dto.LikePostDto;
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

    @Autowired
    public LikeService(LikePostRepos likePostRepos, LikeCommentRepos likeCommentRepos,
                       PostRepos postRepos, CommentRepos commentRepos,
                        LikePostMapper likePostMapper, LikeCommentMapper likeCommentMapper) {
        this.likePostRepos = likePostRepos;
        this.likeCommentRepos = likeCommentRepos;
        this.postRepos = postRepos;
        this.commentRepos = commentRepos;
        this.likePostMapper = likePostMapper;
        this.likeCommentMapper = likeCommentMapper;
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likePostDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'L')")
    public Long likeAPost(LikePostDto likePostDto) {

        LikePost like = likePostMapper.toLikePost(likePostDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));

        LikePost likePost = likePostRepos.findLikeByLikedPost_IdAndOwner_Id(likePostDto.getLikedPost().getId(), likePostDto.getOwnerId()).orElse(null);

        if(likePost!=null)
        {
            throw new ApiSpecificException("Post cannot be liked more than once");
        }

        post.setNoOfLikes(post.getNoOfLikes() + 1);
        LikePost responseLikePost = likePostRepos.save(like);
        return responseLikePost.getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likeCommentDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'Q')")
    public Long likeComment(LikeCommentDto likeCommentDto) {

        LikeComment like = likeCommentMapper.toLikeComment(likeCommentDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));

        LikeComment likeComment = likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(like.getLikedComment().getId(),
                like.getOwner().getId()).orElse(null);
        if (likeComment != null) {
            throw new ApiSpecificException("Comment cannot be liked more than once");
        }

        comment.setNoOfLikes(comment.getNoOfLikes() + 1);
        LikeComment responseLikeComment = likeCommentRepos.save(like);
        return responseLikeComment.getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likePostDto, null)")
    public Long unlikePost(LikePostDto likePostDto) {

        LikePost like = likePostMapper.toLikePost(likePostDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));
        Post post = postRepos.findById(like.getLikedPost().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));
        post.setNoOfLikes(post.getNoOfLikes() - 1);
        LikePost likePost = likePostRepos.findLikeByLikedPost_IdAndOwner_Id(like.getLikedPost().getId(), like.getOwner().getId()).orElseThrow(() ->
                new ApiSpecificException("Nothing to unlike"));

        likePostRepos.deleteById(likePost.getId());
        return likePost.getId();
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#likeCommentDto, null)")
    public Long unlikeComment(LikeCommentDto likeCommentDto) {

        LikeComment like = likeCommentMapper.toLikeComment(likeCommentDto);

        Optional.ofNullable(like).orElseThrow(() -> new ApiSpecificException("Something is wrong"));

        LikeComment likeComment = likeCommentRepos.findLikeByLikedComment_IdAndOwner_Id(like.getLikedComment().getId(),
                like.getOwner().getId()).orElseThrow(() -> new ApiSpecificException("Nothing to unlike"));
        Comment comment = commentRepos.findById(like.getLikedComment().getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));
        comment.setNoOfLikes(comment.getNoOfLikes() - 1);
        likeCommentRepos.deleteById(like.getId());
        return likeComment.getId();

    }

    public DataList<LikePostDto> getLikesOnPost(long postId) {
        List<LikePost> likes =  likePostRepos.findLikesByLikedPost_Id(postId).orElseThrow(() -> new ApiSpecificException("No likes on post"));
        return new DataList<LikePostDto>(likePostMapper.toLikePostDtoList(likes));
    }

    public DataList<LikeCommentDto> getLikesOnComment(long commentId) {
        List<LikeComment> likes = likeCommentRepos.findLikesByLikedComment_Id(commentId).orElseThrow(() -> new ApiSpecificException("No likes on comment"));
        return new DataList<LikeCommentDto>(likeCommentMapper.toLikeCommentDtoList(likes));
    }


}
