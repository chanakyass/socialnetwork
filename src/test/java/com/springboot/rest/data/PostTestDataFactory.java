package com.springboot.rest.data;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostTestDataFactory {

    private final PostService postService;
    private final UserTestDataFactory userTestDataFactory;
    private final HashMap<String, Object> resourceHashMap;
    private final List<PostDto> postListInDb;

    @Autowired
    public PostTestDataFactory(PostService postService, UserTestDataFactory userTestDataFactory,
                               @Qualifier("testExistingResources") HashMap<String, Object> resourceHashMap,
                               @Qualifier("testPostsListForRead") List<PostDto> postListInDb)
    {
        this.postService = postService;
        this.userTestDataFactory = userTestDataFactory;
        this.resourceHashMap = resourceHashMap;
        this.postListInDb = postListInDb;
    }

    private PostDto createNewPost()
    {
        String body = "post body";
        String heading = "post heading";
        Long noOfLikes = 0L;
        LocalDateTime postedOnDate = LocalDateTime.now();


        PostDto latest = new PostDto();
        latest.setPostBody(body);
        latest.setPostHeading(heading);
        latest.setNoOfLikes(noOfLikes);
        latest.setPostedAtTime(postedOnDate);
        return latest;
    }


    public PostDto createPostForLoggedInUser()
    {
        PostDto post = createNewPost();
        post.setOwner(userTestDataFactory.getLoggedInUser());
        return post;
    }

    public PostDto createPostForOtherUser()
    {
        PostDto post = createNewPost();
        post.setOwner(userTestDataFactory.getOtherThanLoggedInUser());
        return post;
    }

    public Long createPostForLoggedInUserAndInsertInDB()
    {
        PostDto postDto = createPostForLoggedInUser();
        ResponseEntity<ApiMessageResponse> message = postService.addPost(postDto);
        return Objects.requireNonNull(message.getBody()).getResourceId();
    }

    public PostDto getPreExistingPost()
    {
        return (PostDto) resourceHashMap.get("EXISTING_POST");
    }

    public List<PostDto> getPostsOfUser(UserDto user)
    {
        return postListInDb.stream().filter((PostDto post) -> post.getId() != null
                && post.getOwner() != null
                && post.getOwner().getId().compareTo(user.getId()) == 0).collect(Collectors.toList());
    }

    public PostDto createPostForLoggedInUserAndAddToDB()
    {
        PostDto post = createPostForLoggedInUser();
        ResponseEntity<ApiMessageResponse> message = postService.addPost(post);
        Long postId = Objects.requireNonNull(message.getBody()).getResourceId();
        return postService.getSelectedPost(postId);
    }

    public void cleanUpPosts()
    {
        postService.deleteAllPosts();
    }

    public void createPostForUser(PostDto post)
    {
        postService.addPost(post);
    }

}
