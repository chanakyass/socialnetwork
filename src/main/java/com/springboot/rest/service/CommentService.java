package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.mapper.CommentMapper;
import com.springboot.rest.model.projections.CommentView;
import com.springboot.rest.repository.CommentRepos;
import com.springboot.rest.repository.PostRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final PostRepos postRepos;
    private final CommentRepos commentRepos;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepos commentRepos, PostRepos postRepos, CommentMapper commentMapper) {
        this.commentRepos = commentRepos;
        this.postRepos = postRepos;
        this.commentMapper = commentMapper;


    }
    @PreAuthorize(value = "hasPermission(#comment, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject.body.resourceId, 'C')")
    public ResponseEntity<ApiMessageResponse> addCommentOnActivity(Comment comment) {
        Optional.ofNullable(comment).orElseThrow(() -> new ApiSpecificException("Some issue with your comment. Please check again"));
        postRepos.findById(comment.getCommentedOn().getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));
        if (comment.getParentComment() != null) {
            commentRepos.findById(comment.getParentComment().getId()).orElseThrow(() -> new ApiSpecificException("Parent Comment is not present"));
        }
        Comment responseComment = commentRepos.save(comment);
        return ResponseEntity.ok().body(new ApiMessageResponse(responseComment.getId()));
    }

    public List<CommentView> getCommentsOnPost(Long postId, int pageNo) {
        Page<CommentView> page = commentRepos.findCommentsByCommentedOn_IdAndParentCommentIsNull(postId,
                (Pageable) PageRequest.of(pageNo, 10, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException("Post is not present"));
        if (page.getTotalElements() > 0)
            return page.getContent();
        else throw new ApiSpecificException("No more comments to show");
    }

    public List<CommentView> getRepliesOnComment(Long commentId, int pageNo) {
        commentRepos.findById(commentId).orElseThrow(() -> new ApiSpecificException("Parent Comment is not present"));
        Page<CommentView> page = commentRepos.findCommentsByParentComment_Id(commentId,
                (Pageable) PageRequest.of(pageNo, 10, Sort.by("noOfLikes")))
                .orElseThrow(() -> new ApiSpecificException("No replies on comment"));
        if (page.getTotalElements() > 0)
            return page.getContent();
        else throw new ApiSpecificException("No more comments to show");
    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#comment, null)")
    public ResponseEntity<ApiMessageResponse> delCommentOnActivity(Comment comment) {
        commentRepos.findById(comment.getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));

        commentRepos.deleteById(comment.getId());

        return ResponseEntity.ok().body(new ApiMessageResponse(comment.getId()));
    }


    @Transactional
    @PreAuthorize(value = "hasPermission(#commentDto, null)")
    public ResponseEntity<ApiMessageResponse> changeCommentOnActivity(CommentDto commentDto) {
        if (commentDto.getCommentContent().isEmpty()) {
            throw new ApiSpecificException("Please add some comment content");
        }

        Comment comment = commentRepos.findById(commentDto.getId()).orElseThrow(() -> new ApiSpecificException("Comment is not present"));

        commentDto.setModifiedOnDate(LocalDate.now());
        commentMapper.update(commentDto, comment);
        return ResponseEntity.ok().body(new ApiMessageResponse(commentDto.getId()));
    }
}
