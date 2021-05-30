package com.springboot.rest.unit.data;

import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.projections.PostView;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PostTestDataFactory {
    private final UserTestDataFactory userTestDataFactory;

    public PostTestDataFactory(UserTestDataFactory userTestDataFactory) {
        this.userTestDataFactory = userTestDataFactory;
    }

    public Post getRandomPost(){
        return new Post(1L, userTestDataFactory.getRandomUser(), "Post heading",
                "Post body", LocalDateTime.now(), null);
    }

    private Post getRandomPost(Long i){
        return new Post(i, userTestDataFactory.getRandomUser(), "Post heading "+i,
                "Post body "+i, LocalDateTime.now(), null);
    }

    public PostDto getRandomPostDto(){
        return new PostDto(1L, userTestDataFactory.getRandomUserProxyDto(), "Post heading",
                "Post body", LocalDateTime.now(), null, 0L, 0L, false);
    }

    public PostDto getRandomPostDto(Long i){
        return new PostDto(i, userTestDataFactory.getRandomUserProxyDto(), "Post heading "+i,
                "Post body "+i, LocalDateTime.now(), null, 0L, 0L, false);
    }

    public List<PostDto> getRandomListOfPostDtos(){
        List<PostDto> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomPostDto(i));
        }
        return list;
    }

    public PostEditDto getRandomPostEditDto(){
        PostDto postDto = getRandomPostDto();
        PostEditDto postEditDto = new PostEditDto();
        postEditDto.setId(postDto.getId());
        postEditDto.setPostHeading(postDto.getPostHeading());
        postEditDto.setPostBody(postDto.getPostBody());
        postEditDto.setModifiedAtTime(postDto.getModifiedAtTime());
        postEditDto.setOwner(postDto.getOwner());
        return postEditDto;
    }

    public static void editPost(PostEditDto postEditDto, Post post){
        postEditDto.setPostBody("Updated post body");
        post.setPostBody("Updated post body");
        postEditDto.setModifiedAtTime(LocalDateTime.now());
        post.setModifiedAtTime(LocalDateTime.now());
    }

    public PostView getRandomPostView(){
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        PostView projection = factory.createProjection(PostView.class);
        projection.setPost(getRandomPost());
        projection.setPostLikedByCurrentUser(false);
        projection.setNoOfComments(0L);
        projection.setNoOfLikes(0L);
        return projection;
    }

    public PostView getRandomPostView(Long i){
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        PostView projection = factory.createProjection(PostView.class);
        projection.setPost(getRandomPost(i));
        projection.setPostLikedByCurrentUser(false);
        projection.setNoOfComments(0L);
        projection.setNoOfLikes(0L);
        return projection;
    }

    public List<PostView> getListOfPostViews(){
        List<PostView> postViewList = new ArrayList<>();
        for(long i=1; i<=10; i++){
            postViewList.add(getRandomPostView(i));
        }
        return postViewList;
    }

}
