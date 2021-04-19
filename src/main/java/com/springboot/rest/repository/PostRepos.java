package com.springboot.rest.repository;

import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepos extends JpaRepository<Post, Long> {

    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id" +
            " left outer join Comment comment on p.id = comment.commentedOn.id where p.owner.id = :userId group by p")
    Optional<Page<Post>> findPostsByOwner_Id(long userId, Pageable pageable);


    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id" +
            " left outer join Comment comment on p.id = comment.commentedOn.id group by p")
    Optional<Page<PostView>> findPostsForUserFeed(Pageable pageable);

    @Query(" select p as post, count(distinct likePost) as noOfLikes, count(distinct comment) as noOfComments from Post p " +
            "left outer join LikePost likePost on p.id = likePost.likedPost.id" +
            " left outer join Comment comment on p.id = comment.commentedOn.id where p.id= :postId group by p")
    Optional<Post> findPostById(long postId);

}



