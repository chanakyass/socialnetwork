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

    @Query("select post, count(comment) as noOfComments, count(likePost) as noOfLikes " +
            "from Post post left outer join Comment comment on post.id = comment.commentedOn.id " +
            "left outer join LikePost likePost on post.id = likePost.likedPost.id where post.owner.id=:userId group by post")
    Optional<Page<PostView>> findPostsByOwner_Id(long userId, Pageable pageable);

    @Query("select post, count(comment) as noOfComments, count(likePost) as noOfLikes " +
            "from Post post left outer join Comment comment on post.id = comment.commentedOn.id " +
            "left outer join LikePost likePost on post.id = likePost.likedPost.id group by post")
    Optional<Page<PostView>> findPostsForUserFeedBy(Pageable pageable);

    @Query("select post,  count(comment) as noOfComments, count(likePost) as noOfLikes " +
            "from Post post left outer join Comment comment left outer join LikePost likePost group by post")
    Optional<PostView> findPostById(long postId);

}



