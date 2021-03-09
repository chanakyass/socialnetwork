package com.springboot.rest.data;

import com.springboot.rest.model.entities.Post;
import com.springboot.rest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PostTestDataFactory {

    private final PostService postService;
    private final UserTestDataFactory userTestDataFactory;
    private final HashMap<String, Post> postsHashMap;

    @Autowired
    public PostTestDataFactory(PostService postService, UserTestDataFactory userTestDataFactory, HashMap<String, Post> postsHashMap)
    {
        this.postService = postService;
        this.userTestDataFactory = userTestDataFactory;
        this.postsHashMap = postsHashMap;
    }

    public Post createPostForLoggedInUser(String purpose)
    {
        Post post = postsHashMap.get(purpose);
        post.setOwner(userTestDataFactory.getLoggedInUser());
        return post;
    }

    public Post createPostForOtherUser(String purpose)
    {
        Post post = postsHashMap.get(purpose);

        post.setOwner(userTestDataFactory.getOtherThanLoggedInUser());
        return post;
    }

    public Post getPreExistingPost()
    {
        return postsHashMap.get("UPDATE_SUCCESS");
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
