package com.springboot.rest.repository;

import com.springboot.rest.model.entities.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikePostRepos extends JpaRepository<LikePost, Long> {

    Optional<List<LikePost>> findLikesByLikedPost_Id(long postId);

    Optional<LikePost> findLikeByLikedPost_IdAndOwner_Id(long postId, long userId);


}
