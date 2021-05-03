package com.springboot.rest.service;

import com.springboot.rest.config.exceptions.ApiResourceNotFoundException;
import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.config.security.SecurityUtils;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.dto.response.DataList;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.PostEditMapper;
import com.springboot.rest.model.mapper.PostMapper;
import com.springboot.rest.model.projections.PostView;
import com.springboot.rest.repository.OffsetBasedPageRequest;
import com.springboot.rest.repository.PostRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepos postRepos;
    private final UserRepos userRepos;
    private final PostEditMapper postEditMapper;
    private final PostMapper postMapper;
    private final LikeService likeService;
    private final SecurityUtils securityUtils;

    @Autowired
    public PostService(PostRepos postRepos, UserRepos userRepos, PostEditMapper postEditMapper, PostMapper postMapper, LikeService likeService, SecurityUtils securityUtils) {
        this.postRepos = postRepos;
        this.userRepos = userRepos;
        this.postEditMapper = postEditMapper;
        this.postMapper = postMapper;
        this.likeService = likeService;
        this.securityUtils = securityUtils;

    }

    @PreAuthorize(value = "hasPermission(#postDto, null)")
    @PostAuthorize(value = "@authorizationService.saveSecureResource(returnObject, 'P')")
    public Long addPost(PostDto postDto) {
        Optional.ofNullable(postDto).orElseThrow(() -> new ApiSpecificException("Some problem in your post"));

        Post post = postMapper.toPost(postDto);
        Post responsePost = postRepos.save(post);

        return responsePost.getId();
    }

    public DataList<PostDto> getPosts(int pageNo, int noOfDeletions) {
        long nextOffset = pageNo * 10L
                - (long) noOfDeletions;
        int limit = 10;


        Pageable pageable =  new OffsetBasedPageRequest(nextOffset, limit,
                Sort.by("noOfLikes").descending());

        Page<PostView> page = postRepos.findPostsForUserFeed(
                securityUtils.getSubjectId(),
                pageable)
                .orElseThrow(() -> new ApiSpecificException("There are no posts to show"));

        return new DataList<>(postMapper.toPostDtoListFromView(page.getContent()), page.getTotalPages(), pageNo);

    }

    public PostDto getSelectedPost(Long Id) {
        PostView post =  postRepos.findPostWithId(securityUtils.getSubjectId(), Id).orElseThrow(ApiResourceNotFoundException::new);
        return postMapper.toPostDtoFromView(post);

    }


    public DataList<PostDto> getPostsOfUser(Long userId, int pageNo, int noOfDeletions) {

        long nextOffset = pageNo * 10L
                - (long) noOfDeletions;
        int limit = 10;
        Pageable pageable =  new OffsetBasedPageRequest(nextOffset, limit,
                Sort.by("noOfLikes").descending());

        userRepos.findById(userId).orElseThrow(ApiResourceNotFoundException::new);
        Page<PostView> page = postRepos.findPostsOfOwner(userId, securityUtils.getSubjectId(),
                pageable)
                .orElseThrow(() -> new ApiSpecificException(("No posts by user")));

        List<PostDto> listOfPosts =  postMapper.toPostDtoListFromView(page.getContent());
        return new DataList<>(listOfPosts, page.getTotalPages(), pageNo);


    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#postEditDto, null)")
    public Long updatePost(PostEditDto postEditDto) {
        Post postToUpdate = postRepos.findById(postEditDto.getId()).orElseThrow(ApiResourceNotFoundException::new);
        postEditMapper.toPost(postEditDto, postToUpdate);
        return postEditDto.getId();

    }

    @Transactional
    @PreAuthorize(value = "hasPermission(#postId, \"Post\", null)")
    @PostAuthorize(value = "@authorizationService.deleteSecureResource(returnObject, 'P')")
    public Long deletePost(Long postId) {
        postRepos.findById(postId).orElseThrow(ApiResourceNotFoundException::new);
        postRepos.deleteById(postId);
        return postId;
    }

    @Transactional
    public void deleteAllPosts()
    {
        postRepos.deleteAll();
    }
}
