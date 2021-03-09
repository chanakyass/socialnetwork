package com.springboot.rest.repository;

import com.springboot.rest.model.entities.SecureResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecureResourceRepos extends JpaRepository<SecureResource, Long> {

    @Query("select resource from SecureResource resource where resource.comment.id=:id or resource.post.id=:id or resource.likePost.id=:id or resource.likeComment.id=:id")
    Optional<SecureResource> findByResourceTypeAndId(Long id) ;

}
