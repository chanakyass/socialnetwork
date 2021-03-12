package com.springboot.rest.repository;

import com.springboot.rest.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepos extends JpaRepository<Post, Long> {

    Optional<Page<Post>> findPostsByOwner_Id(long userId, Pageable pageable);

    Optional<Page<Post>> findPostsForUserFeedBy(Pageable pageable);

    Optional<Post> findPostById(long postId);

}



