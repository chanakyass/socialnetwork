package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.PostMapper;
import com.springboot.rest.model.projections.PostView;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepos postRepos;
    private final UserRepos userRepos;
    private final PostMapper postMapper;
    private final CommentService commentService;

    @Autowired
    public PostService(PostRepos postRepos, UserRepos userRepos, PostMapper postMapper, CommentService commentService) {
        this.postRepos = postRepos;
        this.userRepos = userRepos;
        this.postMapper = postMapper;
        this.commentService = commentService;

    }

    @PreAuthorize(value = "hasPermission(#post, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject.body.resourceId, 'P')")
    public ResponseEntity<ApiMessageResponse> addPost(Post post) {
        Optional.ofNullable(post).orElseThrow(() -> new ApiSpecificException("Some problem in your post"));
        Post responsePost = postRepos.save(post);
        return ResponseEntity.ok().body(new ApiMessageResponse(responsePost.getId()));
    }

    public List<PostView> getPosts(int pageNo) {
        Page<PostView> page = postRepos.findPostsForUserFeedBy(
                (Pageable) PageRequest.of(pageNo, 10, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException("There are no posts to show"));
        if (page.getTotalElements() > 0)
            return page.getContent();
        else throw new ApiSpecificException("No more pages to show");

    }

    public PostView getSelectedPost(Long Id) {
        return postRepos.findPostById(Id).orElseThrow(() -> new ApiSpecificException(("The post is not present")));
    }

    public List<PostView> getPostsOfUser(Long userId, int pageNo) {
        userRepos.findById(userId).orElseThrow(() -> new ApiSpecificException("User is not present"));
        Page<PostView> page = postRepos.findPostsByOwner_Id(userId,
                (Pageable) PageRequest.of(pageNo, 10, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException(("No posts by user")));
        if (page.getTotalElements() > 0)
            return page.getContent();
        else throw new ApiSpecificException("No more pages to show");

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#postDto, null)")
    public ResponseEntity<ApiMessageResponse> updatePost(PostDto postDto) {
        Post postToUpdate = postRepos.findById(postDto.getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));

        postDto.setModifiedOnDate(LocalDate.now());
        postMapper.update(postDto, postToUpdate);
        return ResponseEntity.ok().body(new ApiMessageResponse(postDto.getId()));

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#post, null)")
    public ResponseEntity<ApiMessageResponse> deletePost(Post post) {
        postRepos.findById(post.getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));


        postRepos.deleteById(post.getId());
        return ResponseEntity.ok().body(new ApiMessageResponse(post.getId()));
    }

    @Transactional
    public void deleteAllPosts()
    {
        postRepos.deleteAll();
    }
}
