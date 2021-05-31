package com.springboot.rest.service.integration.data;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.response.Data;
import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
        return postService.addPost(postDto);
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

    public Data<PostDto> createPostForLoggedInUserAndAddToDB()
    {
        PostDto post = createPostForLoggedInUser();
        Long postId =  postService.addPost(post);
        return new Data<>(postService.getSelectedPost(postId));
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
