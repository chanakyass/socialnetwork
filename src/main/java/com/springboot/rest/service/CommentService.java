package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.CommentEditMapper;
import com.springboot.rest.model.mapper.CommentMapper;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.PostRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    public CommentService(CommentRepos commentRepos, PostRepos postRepos, CommentEditMapper commentEditMapper,CommentMapper commentMapper) {
        this.commentRepos = commentRepos;
        this.postRepos = postRepos;
        this.commentEditMapper = commentEditMapper;
        this.commentMapper = commentMapper;


    }
    @PreAuthorize(value = "hasPermission(#commentDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'C')")
    public Long addCommentOnActivity(CommentDto commentDto) {

        Optional.ofNullable(commentDto).orElseThrow(() -> new ApiSpecificException("Some issue with your comment. Please check again"));

        Comment comment = commentMapper.toComment(commentDto);
        Post parentPost = postRepos.findById(commentDto.getCommentedOn().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));
        if (commentDto.getParentComment() != null) {
            Comment parentComment = commentRepos.findById(commentDto.getParentComment().getId()).orElseThrow(() -> new ApiSpecificException("Parent Comment is not present"));
            parentComment.setNoOfReplies(parentComment.getNoOfReplies()+1);
        }
        else{
            parentPost.setNoOfComments(parentPost.getNoOfComments()+1);
        }
        Comment responseComment = commentRepos.save(comment);
        return responseComment.getId();
    }

    public DataList<CommentDto> getCommentsOnPost(Long postId, int pageNo) {
        Page<Comment> page = commentRepos.findCommentsByCommentedOn_IdAndParentCommentIsNull(postId,
                 PageRequest.of(pageNo, 5, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException("Post is not present"));

        return new DataList<>(commentMapper.toCommentDtoList(page.getContent()), page.getTotalPages(), pageNo);


    }

    public DataList<CommentDto> getRepliesOnComment(Long commentId, int pageNo) {
        commentRepos.findById(commentId).orElseThrow(() -> new ApiSpecificException("Parent Comment is not present"));
        Page<Comment> page = commentRepos.findCommentsByParentComment_Id(commentId,
                 PageRequest.of(pageNo, 5, Sort.by("noOfLikes")))
                .orElseThrow(() -> new ApiSpecificException("No replies on comment"));

        return new DataList<>(commentMapper.toCommentDtoList(page.getContent()), page.getTotalPages(), pageNo);

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#commentId, \"Comment\", null)")
    @PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject, 'C')")
    public Long delCommentOnActivity(Long commentId) {
        Comment comment = commentRepos.findById(commentId).orElseThrow(() -> new ApiSpecificException("Comment is not present"));
        Comment parentComment = comment.getParentComment();
        if(parentComment != null) {
            parentComment.setNoOfReplies(parentComment.getNoOfReplies() - 1);
        }
        else{
            Post post = comment.getCommentedOn();
            post.setNoOfComments(post.getNoOfComments() - 1);
        }
        commentRepos.deleteById(commentId);

        return commentId;
    }



    @Transactional
    @PreAuthorize(value = "hasPermission(#commentEditDto, null)")
    public Long changeCommentOnActivity(CommentEditDto commentEditDto) {
        if (commentEditDto.getCommentContent().isEmpty()) {
            throw new ApiSpecificException("Please add some comment content");
        }

        Comment comment = commentRepos.findById(commentEditDto.getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));
        commentEditMapper.update(commentEditDto, comment);
        return commentEditDto.getId();
    }
}
