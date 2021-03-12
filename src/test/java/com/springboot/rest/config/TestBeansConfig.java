package com.springboot.rest.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.dto.LikeCommentDto;
import com.springboot.rest.model.dto.LikePostDto;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@TestConfiguration
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestBeansConfig {


    public static User getOtherUserInContext() {
        return getTestUsersMap().get("ANOTHER_USER");
    }

    public static User getThirdUserInContext()
    {
        return getTestUsersMap().get("THIRD_USER");
    }

    public static User getUserAsPerChoice(String userKey)
    {
        return getTestUsersMap().get(userKey);
    }

    @Bean("testExistingResources")
    public static HashMap<String, Object> getResourcesMap()
    {
        String pathToTestPosts="src/test/resources/json/write-related/posts.json";
        String pathToTestComments="src/test/resources/json/write-related/comments.json";
        String pathToTestLikesOnPosts="src/test/resources/json/write-related/like-post.json";
        String pathToTestLikesOnComments="src/test/resources/json/write-related/like-comment.json";

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> resourcesHashMap = new HashMap<>();

        try {

            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestPosts);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<HashMap<String, PostDto>> typeRef
                    = new TypeReference<>() {
            };
            HashMap<String, PostDto> postHashMap = objectMapper.readValue(jsonParser, typeRef);

            resourcesHashMap.putAll(postHashMap);

            fileReader = new FileReader(pathToTestComments);
            jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<HashMap<String, CommentDto>>typeRef2 = new TypeReference<>() {
            };
            HashMap<String, CommentDto> commentHashMap = objectMapper.readValue(jsonParser, typeRef2);

            resourcesHashMap.putAll(commentHashMap);

            fileReader = new FileReader(pathToTestLikesOnPosts);
            jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<HashMap<String, LikePostDto>>typeRef3 = new TypeReference<>() {
            };
            HashMap<String, LikePostDto> likePostHashMap = objectMapper.readValue(jsonParser, typeRef3);

            resourcesHashMap.putAll(likePostHashMap);

            fileReader = new FileReader(pathToTestLikesOnComments);
            jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<HashMap<String, LikeCommentDto>>typeRef4 = new TypeReference<>() {
            };
            HashMap<String, LikeCommentDto> likeCommentHashMap = objectMapper.readValue(jsonParser, typeRef4);

            resourcesHashMap.putAll(likeCommentHashMap);




        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return resourcesHashMap;
    }


    @Bean(name = "testUsers")
    public static HashMap<String, User> getTestUsersMap(/*@Value("${springboot.test.config.path}"*/)
    {
        String pathToTestUsers="src/test/resources/json/user-profiles.json";
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, User> userHashMap = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(new FileReader(pathToTestUsers));
            TypeReference<HashMap<String, User>> typeRef
                    = new TypeReference<>() {
            };
            userHashMap = objectMapper.readValue(jsonParser, typeRef);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return userHashMap;
    }

    @Bean(name = "testCommentsListForRead")
    public static List<CommentDto> getTestCommentsList(/*@Value("${springboot.test.config.path}")*/)
    {
        String pathToTestComments="src/test/resources/json/read-related/comments_for_get.json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<CommentDto> commentsList = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestComments);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<List<CommentDto>> typeRef
                    = new TypeReference<>() {
            };
            commentsList = objectMapper.readValue(jsonParser, typeRef);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return commentsList;
    }

    @Bean(name = "testPostsListForRead")
    public static List<PostDto> getTestPostsList(/*@Value("${springboot.test.config.path}")*/)
    {
        String pathToTestPosts="src/test/resources/json/read-related/posts_for_get.json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<PostDto> postsList = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestPosts);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<List<PostDto>> typeRef
                    = new TypeReference<>() {
            };
            postsList = objectMapper.readValue(jsonParser, typeRef);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return postsList;
    }

    @Bean(name = "testLikePostsListForRead")
    public static List<LikePostDto> getTestLikesOnPostsList(/*@Value("${springboot.test.config.path}")*/)
    {
        String pathToTestPosts="src/test/resources/json/read-related/likes_on_posts_for_get.json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<LikePostDto> likesList = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestPosts);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<List<LikePostDto>> typeRef
                    = new TypeReference<>() {
            };
            likesList = objectMapper.readValue(jsonParser, typeRef);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return likesList;
    }

    @Bean(name = "testLikeCommentsListForRead")
    public static List<LikeCommentDto> getTestLikesOnCommentsList(/*@Value("${springboot.test.config.path}")*/)
    {
        String pathToTestLikes="src/test/resources/json/read-related/likes_on_comments_for_get.json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<LikeCommentDto> likesList = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestLikes);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            TypeReference<List<LikeCommentDto>> typeRef
                    = new TypeReference<>() {
            };
            likesList = objectMapper.readValue(jsonParser, typeRef);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return likesList;
    }
}

