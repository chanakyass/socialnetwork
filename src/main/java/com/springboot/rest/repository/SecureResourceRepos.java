package com.springboot.rest.repository;

import com.springboot.rest.model.entities.SecureResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecureResourceRepos extends JpaRepository<SecureResource, Long> {

    @Query("select resource from SecureResource resource where resource.post.id=:id")
    Optional<SecureResource> findByPost_Id(Long id) ;
    @Query("select resource from SecureResource resource where resource.comment.id=:id")
    Optional<SecureResource> findByComment_Id(Long id) ;
    @Query("select resource from SecureResource resource where resource.likePost.id=:id and resource.owner.id=:ownerId")
    Optional<SecureResource> findByLikePost_IdAndOwner_Id(Long id, Long ownerId) ;
    @Query("select resource from SecureResource resource where resource.likeComment.id=:id and resource.owner.id=:ownerId")
    Optional<SecureResource> findByLikeComment_IdAndOwner_Id(Long id, Long ownerId) ;
    boolean existsSecureResourceByLikePost_Id(Long id);
    boolean existsSecureResourceByLikeComment_Id(Long id);


    //delete
    @Modifying
    @Query("delete from SecureResource resource where resource.post.id=:id")
    void deleteDistinctByPost_Id(Long id);
    @Modifying
    @Query("delete from SecureResource resource where resource.comment.id=:id")
    void deleteDistinctByComment_Id(Long id);
    @Modifying
    @Query("delete from SecureResource resource where resource.likePost.id=:id and resource.owner.id=:ownerId")
    void deleteDistinctByLikePost_IdAndOwner_Id(Long id, Long ownerId);
    @Modifying
    @Query("delete from SecureResource resource where resource.likeComment.id=:id and resource.owner.id=:ownerId")
    void deleteDistinctByLikeComment_IdAndOwner_Id(Long id, Long ownerId);


}
