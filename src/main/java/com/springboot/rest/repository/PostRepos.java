package com.springboot.rest.repository;

import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepos extends JpaRepository<Post, Long> {

    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments, " +
            "(case when (likePost2.owner.id = :loggedInUserId) then true else false end) as postLikedByCurrentUser " +
            "from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id " +
            "left outer join Comment comment on p.id = comment.commentedOn.id " +
            "left outer join LikePost likePost2 on p.id = likePost2.likedPost.id and likePost2.owner.id =:loggedInUserId " +
            "where p.owner.id = :userId group by p, likePost2.owner.id")
    Optional<Page<PostView>> findPostsOfOwner(Long userId, Long loggedInUserId, Pageable pageable);


    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments, " +
            "(case when (likePost2.owner.id = :loggedInUserId) then true else false end) as postLikedByCurrentUser " +
            "from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id " +
            "left outer join Comment comment on p.id = comment.commentedOn.id " +
            "left outer join LikePost likePost2 on p.id = likePost2.likedPost.id and likePost2.owner.id =:loggedInUserId " +
            "group by p, likePost2.owner.id ")
    Optional<Page<PostView>> findPostsForUserFeed(Long loggedInUserId, Pageable pageable);

    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments, " +
            "(case when (likePost2.owner.id = :loggedInUserId) then true else false end) as postLikedByCurrentUser " +
            "from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id" +
            " left outer join Comment comment on p.id = comment.commentedOn.id " +
            "left outer join LikePost likePost2 on p.id = likePost2.likedPost.id and likePost2.owner.id =:loggedInUserId " +
            "where p.id= :postId group by p, likePost2.owner.id")
    Optional<PostView> findPostWithId(Long loggedInUserId, Long postId);

}



