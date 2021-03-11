package com.springboot.rest.data;

import com.springboot.rest.model.dto.ApiMessageResponse;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.projections.PostView;
import com.springboot.rest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostTestDataFactory {

    private final PostService postService;
    private final UserTestDataFactory userTestDataFactory;
    private final HashMap<String, Object> resourceHashMap;
    private final List<Post> postListInDb;

    @Autowired
    public PostTestDataFactory(PostService postService, UserTestDataFactory userTestDataFactory,
                               @Qualifier("testExistingResources") HashMap<String, Object> resourceHashMap,
                               @Qualifier("testPostsListForRead") List<Post> postListInDb)
    {
        this.postService = postService;
        this.userTestDataFactory = userTestDataFactory;
        this.resourceHashMap = resourceHashMap;
        this.postListInDb = postListInDb;
    }

    private Post createNewPost()
    {
        String body = "post body";
        String heading = "post heading";
        Long noOfLikes = 0L;
        LocalDate postedOnDate = LocalDate.of(2020, 5, 10);


        Post latest = new Post();
        latest.setPostBody(body);
        latest.setPostHeading(heading);
        latest.setNoOfLikes(noOfLikes);
        latest.setPostedOnDate(postedOnDate);
        return latest;
    }

    public Post createPostForLoggedInUser()
    {
        Post post = createNewPost();
        post.setOwner(userTestDataFactory.getLoggedInUser());
        return post;
    }

    public Post createPostForOtherUser()
    {
        Post post = createNewPost();
        post.setOwner(userTestDataFactory.getOtherThanLoggedInUser());
        return post;
    }

    public Post getPreExistingPost()
    {
        return (Post) resourceHashMap.get("EXISTING_POST");
    }

    public List<Post> getPostsOfUser(User user)
    {
        return postListInDb.stream().filter((Post post) -> post.getId() != null
                && post.getOwner() != null
                && post.getOwner().getId().compareTo(user.getId()) == 0).collect(Collectors.toList());
    }

    public PostView createPostForLoggedInUserAndAddToDB()
    {
        Post post = createPostForLoggedInUser();
        ResponseEntity<ApiMessageResponse> message = postService.addPost(post);
        Long postId = Objects.requireNonNull(message.getBody()).getResourceId();
        return postService.getSelectedPost(postId);
    }

    public void cleanUpPosts()
    {
        postService.deleteAllPosts();
    }

    public void createPostForUser(Post post)
    {
        postService.addPost(post);
    }

}
