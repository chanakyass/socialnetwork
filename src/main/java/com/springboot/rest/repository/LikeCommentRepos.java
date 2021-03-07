package com.springboot.rest.repository;

import com.springboot.rest.model.entities.LikeComment;
import com.springboot.rest.model.projections.LikeCommentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeCommentRepos extends JpaRepository<LikeComment, Long> {

    Optional<List<LikeCommentView>> findLikesByLikedComment_Id(long commentId);

    Optional<LikeCommentView> findLikeByLikedComment_IdAndOwner_Id(long commentId, long userId);

}
