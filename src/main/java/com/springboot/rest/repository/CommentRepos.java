package com.springboot.rest.repository;

import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.projections.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepos extends JpaRepository<Comment, Long> {

    @Query("select c as comment, count(distinct loc) as noOfLikes, count(distinct c2) as noOfReplies, " +
            "(case when (loc2.owner.id = :loggedInUserId) then true else false end) as commentLikedByCurrentUser " +
            "from Comment c " +
            "left outer join LikeComment loc on c.id=loc.likedComment.id " +
            "left outer join Comment c2 on c2.commentPath like CONCAT(c.commentPath, c.id, '/', '%') " +
            "left outer join LikeComment loc2 on loc2.likedComment.id = c.id and loc2.owner.id = :loggedInUserId " +
            "where c.parentComment.id is null and c.commentedOn.id = :postId group by comment, loc2.owner.id ")
    Optional<Page<CommentView>> findLevelOneCommentsOnPost(Long postId, Long loggedInUserId, Pageable pageable);

    @Query("select c as comment, count(distinct loc) as noOfLikes, count(distinct c2) as noOfReplies, " +
            "(case when (loc2.owner.id = :loggedInUserId) then true else false end) as commentLikedByCurrentUser " +
            "from Comment c " +
            "left outer join LikeComment loc on c.id=loc.likedComment.id " +
            "left outer join Comment c2 on c2.commentPath like CONCAT(c.commentPath,c.id, '/', '%') " +
            "left outer join LikeComment loc2 on loc2.likedComment.id = c.id and loc2.owner.id = :loggedInUserId " +
            "where c.parentComment.id = :parentCommentId group by comment, loc2.owner.id ")
    Optional<Page<CommentView>> findCommentsWithParentCommentAs(Long parentCommentId, Long loggedInUserId, Pageable pageable);

    @Query("select c as comment, count(distinct loc) as noOfLikes, count(distinct c2) as noOfReplies, " +
            "(case when (loc2.owner.id = :loggedInUserId) then true else false end) as commentLikedByCurrentUser " +
            "from Comment c " +
            "left outer join LikeComment loc on c.id=loc.likedComment.id " +
            "left outer join Comment c2 on c2.commentPath like CONCAT(c.commentPath, c.id, '/', '%') " +
            "left outer join LikeComment loc2 on loc2.likedComment.id = c.id and loc2.owner.id = :loggedInUserId " +
            "where c.owner.id =:userId group by comment, loc2.owner.id ")
    Optional<List<CommentView>> findAllCommentsOfOwner(Long userId, Long loggedInUserId);


}


