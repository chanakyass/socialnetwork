package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.dto.*;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.PostEditMapper;
import com.springboot.rest.model.mapper.PostMapper;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepos postRepos;
    private final UserRepos userRepos;
    private final PostEditMapper postEditMapper;
    private final PostMapper postMapper;
    private final CommentService commentService;

    @Autowired
    public PostService(PostRepos postRepos, UserRepos userRepos, PostEditMapper postEditMapper, PostMapper postMapper, CommentService commentService) {
        this.postRepos = postRepos;
        this.userRepos = userRepos;
        this.postEditMapper = postEditMapper;
        this.commentService = commentService;
        this.postMapper = postMapper;

    }

    @PreAuthorize(value = "hasPermission(#postDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'P')")
    public Long addPost(PostDto postDto) {
        Optional.ofNullable(postDto).orElseThrow(() -> new ApiSpecificException("Some problem in your post"));

        Post post = postMapper.toPost(postDto);
        Post responsePost = postRepos.save(post);

        return responsePost.getId();
    }

    public DataList<PostDto> getPosts(int pageNo) {
        Page<Post> page = postRepos.findPostsForUserFeedBy(
                (Pageable) PageRequest.of(pageNo, 10, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException("There are no posts to show"));

        PostDto postDto = new PostDto();


        if (page.getTotalElements() > 0) {
            List<Post> posts = page.getContent();
            return new DataList<PostDto>(postMapper.toPostDtoList(posts), page.getTotalPages(), pageNo);
        }
        else throw new ApiSpecificException("No more pages to show");
    }

    public Data<PostDto> getSelectedPost(Long Id) {
        Post post =  postRepos.findPostById(Id).orElseThrow(() -> new ApiSpecificException(("The post is not present")));
        return new Data<PostDto>(postMapper.toPostDto(post));
    }

    public DataList<PostDto> getPostsOfUser(Long userId, int pageNo) {
        userRepos.findById(userId).orElseThrow(() -> new ApiSpecificException("User is not present"));
        Page<Post> page = postRepos.findPostsByOwner_Id(userId,
                PageRequest.of(pageNo, 10, Sort.by("noOfLikes").descending()))
                .orElseThrow(() -> new ApiSpecificException(("No posts by user")));
        if (page.getTotalElements() > 0)
        {
            List<PostDto> listOfPosts =  postMapper.toPostDtoList(page.getContent());
            return new DataList<PostDto>(listOfPosts, page.getTotalPages(), pageNo);
        }
        else throw new ApiSpecificException("No more pages to show");

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#postEditDto, null)")
    public Long updatePost(PostEditDto postEditDto) {
        Post postToUpdate = postRepos.findById(postEditDto.getId()).orElseThrow(() -> new ApiSpecificException("Post is not present"));

        postEditDto.setModifiedAtTime(LocalDateTime.now());
        postEditMapper.toPost(postEditDto, postToUpdate);
        return postEditDto.getId();

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#postId, \"ApiResourceMarker\", null)")
    public Long deletePost(Long postId) {
        postRepos.findById(postId).orElseThrow(() -> new ApiSpecificException("Post is not present"));


        postRepos.deleteById(postId);
        return postId;
    }

    @Transactional
    public void deleteAllPosts()
    {
        postRepos.deleteAll();
    }
}
