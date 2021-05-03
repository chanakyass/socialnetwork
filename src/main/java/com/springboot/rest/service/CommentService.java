package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiResourceNotFoundException;
import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.mapper.CommentEditMapper;
import com.springboot.rest.model.mapper.CommentMapper;
import com.springboot.rest.model.projections.CommentView;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.OffsetBasedPageRequest;
import com.springboot.rest.repository.PostRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentService {

    private final PostRepos postRepos;
    private final CommentRepos commentRepos;
    private final CommentEditMapper commentEditMapper;
    private final CommentMapper commentMapper;
    private final SecurityUtils securityUtils;

    @Autowired
    public CommentService(CommentRepos commentRepos, PostRepos postRepos, CommentEditMapper commentEditMapper,CommentMapper commentMapper, SecurityUtils securityUtils) {
        this.commentRepos = commentRepos;
        this.postRepos = postRepos;
        this.commentEditMapper = commentEditMapper;
        this.commentMapper = commentMapper;
        this.securityUtils = securityUtils;


    }
    @PreAuthorize(value = "hasPermission(#commentDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'C')")
    public Long addCommentOnActivity(CommentDto commentDto) {

        Optional.ofNullable(commentDto).orElseThrow(() -> new ApiSpecificException("Some issue with your comment. Please check again"));

        Comment comment = commentMapper.toComment(commentDto);
        postRepos.findById(commentDto.getCommentedOn().getId())
                .orElseThrow(() -> new ApiResourceNotFoundException("Parent post doesn't exist"));
        if (commentDto.getParentComment() != null) {
            Comment parentComment = commentRepos.findById(commentDto.getParentComment().getId())
                    .orElseThrow(() -> new ApiResourceNotFoundException("Parent comment doesn't exist"));
            comment.setCommentPath(parentComment.getCommentPath()+ parentComment.getId()+"/");
        }
        else{
            comment.setCommentPath("/");
        }

        Comment responseComment = commentRepos.save(comment);
        return responseComment.getId();
    }

    public DataList<CommentDto> getCommentsOnPost(Long postId, int pageNo, int noOfDeletions) {
        long nextOffset = pageNo * 5L
                - (long) noOfDeletions;
        int limit = 5;


        Pageable pageable =  new OffsetBasedPageRequest(nextOffset, limit,
                Sort.by("noOfLikes").descending());

        Page<CommentView> page = commentRepos.findLevelOneCommentsOnPost(postId, securityUtils.getSubjectId(),
                 pageable)
                .orElseThrow(ApiResourceNotFoundException::new);

        return new DataList<>(commentMapper.toCommentDtoListFromView(page.getContent()), page.getTotalPages(), pageNo);
    }

    public DataList<CommentDto> getRepliesOnComment(Long commentId, int pageNo, int noOfDeletions) {
        long nextOffset = pageNo * 5L
                - (long) noOfDeletions;
        int limit = 5;
        Pageable pageable =  new OffsetBasedPageRequest(nextOffset, limit,
                Sort.by("noOfLikes").descending());

        commentRepos.findById(commentId).orElseThrow(ApiResourceNotFoundException::new);
        Page<CommentView> page = commentRepos.findCommentsWithParentCommentAs(commentId, securityUtils.getSubjectId(),
                 pageable)
                .orElseThrow(() -> new ApiSpecificException("No replies on comment"));

        return new DataList<>(commentMapper.toCommentDtoListFromView(page.getContent()), page.getTotalPages(), pageNo);

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#commentId, \"Comment\", null)")
    @PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject, 'C')")
    public Long delCommentOnActivity(Long commentId) {
        Comment comment = commentRepos.findById(commentId).orElseThrow(ApiResourceNotFoundException::new);
        commentRepos.deleteById(commentId);

        return commentId;
    }



    @Transactional
    @PreAuthorize(value = "hasPermission(#commentEditDto, null)")
    public Long changeCommentOnActivity(CommentEditDto commentEditDto) {
        if (commentEditDto.getCommentContent().isEmpty()) {
            throw new ApiSpecificException("Please add some comment content");
        }

        Comment comment = commentRepos.findById(commentEditDto.getId()).orElseThrow(ApiResourceNotFoundException::new);
        commentEditMapper.update(commentEditDto, comment);
        return commentEditDto.getId();
    }
}
