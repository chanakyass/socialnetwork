package com.springboot.rest.repository;

import com.springboot.rest.model.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepos extends JpaRepository<Comment, Long> {

    /*@Query("select c.id as id, c.commentContent as commentContent, c.commentedOnDate as commentedOnDate, c.modifiedOnDate as modifiedOnDate, c.noOfLikes as noOfLikes, c.commentedOn.id as commentedOn_id," +
            " c.owner.id as owner_id, u.name as owner_name, u.email as owner_email from Comment c join User u on c.owner.id = u.id " +
            "where c.commentedOn.id=:postId and c.parentComment is null ")*/
    Optional<Page<Comment>> findCommentsByCommentedOn_IdAndParentCommentIsNull(Long postId, Pageable pageable);

    Optional<Page<Comment>> findCommentsByParentComment_Id(Long parentCommentId, Pageable pageable);



}


