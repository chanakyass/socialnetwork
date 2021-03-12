package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.DemoApplicationTests;
import com.springboot.rest.data.CommentTestDataFactory;
import com.springboot.rest.data.LikeTestDataFactory;
import com.springboot.rest.data.PostTestDataFactory;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class GetServicesTest extends DemoApplicationTests {

    private final MockMvc mockMvc;
    private final CommentTestDataFactory commentTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final UserTestDataFactory userTestDataFactory;
    private final LikeTestDataFactory likeTestDataFactory;


    @Autowired
    public GetServicesTest(MockMvc mockMvc, CommentTestDataFactory commentTestDataFactory,
                           PostTestDataFactory postTestDataFactory, UserTestDataFactory userTestDataFactory,
                            LikeTestDataFactory likeTestDataFactory) {
        this.mockMvc = mockMvc;
        this.commentTestDataFactory = commentTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
        this.likeTestDataFactory = likeTestDataFactory;
    }

    @BeforeAll
    void setup(@Autowired DataSource dataSource,
               @Autowired PlatformTransactionManager transactionManager) {
        try (Connection conn = dataSource.getConnection()) {
            // you'll have to make sure conn.autoCommit = true (default for e.g. H2)
            // e.g. url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;MODE=MySQL
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/read-related/3.sql"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    @AfterAll
    public void fireDown(@Autowired DataSource dataSource,
                         @Autowired PlatformTransactionManager transactionManager)
    {
        try (Connection conn = dataSource.getConnection()) {
            // you'll have to make sure conn.autoCommit = true (default for e.g. H2)
            // e.g. url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;MODE=MySQL
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/read-related/4.sql"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }



    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetCommentsOnPostSuccess() throws Exception
    {
        PostDto post = postTestDataFactory.getPreExistingPost();

        List<CommentDto> commentsOnPost = commentTestDataFactory.getAllCommentsOnPost(post);

        Integer[] idsInInt = commentsOnPost.stream().map((CommentDto comment)->comment.getId().intValue()).toArray(Integer[]::new);
        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/post/%d/comments/0", post.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetRepliesOnCommentSuccess() throws Exception
    {
        CommentDto comment = commentTestDataFactory.getPreExistingComment();

        //get List of replies on comment
        List<CommentDto> repliesOnComment = commentTestDataFactory.getRepliesOnComment(comment);

        //map the comments list to an ids list

        Integer[] idsInInt = repliesOnComment.stream().map(CommentDto::getId).map(Long::intValue).toArray(Integer[]::new);

        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/comment/%d/replies/0", comment.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetPostsOfUserSuccess() throws Exception
    {
        List<PostDto> postsOfUser = postTestDataFactory.getPostsOfUser(userTestDataFactory.getThisUser("THIRD_USER"));
        Integer[] idsInInt = postsOfUser.stream().map(PostDto::getId).map(Long::intValue).toArray(Integer[]::new);
        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/profile/%d/posts/0", userTestDataFactory.getThisUser("THIRD_USER").getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetLikesOnPostsSuccess() throws Exception
    {
        PostDto postDto = postTestDataFactory.getPreExistingPost();
        List<LikePostDto> likes = likeTestDataFactory.getLikesOnPost(postDto);
        Integer[] idsInInt = likes.stream().map(LikePostDto::getId).map(Long::intValue).toArray(Integer[]::new);

        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/post/%d/likes", postDto.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetLikesOnCommentsSuccess() throws Exception
    {
        CommentDto commentDto = commentTestDataFactory.getPreExistingComment();
        List<LikeCommentDto> likes = likeTestDataFactory.getLikesOnComment(commentDto);
        Integer[] idsInInt = likes.stream().map(LikeCommentDto::getId).map(Long::intValue).toArray(Integer[]::new);

        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/comment/%d/likes", commentDto.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();


    }

    @Test
    public void testPostGetWithJwtAuthSuccess() throws Exception
    {
        UserDto user = userTestDataFactory.createUserWithCreds("chanakya@gmail.com", "pass");


        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword());

        String token = "Bearer "+userTestDataFactory.loginAndGetJwtToken(authRequest);

        this.mockMvc.perform(get(String.format("/api/v1/resource/profile/%d/posts/0",
                userTestDataFactory.getThisUser("THIRD_USER").getId())).header("Authorization",token))
                .andExpect(status().isOk());



    }

    @Test
    public void testPostGetWithJwtAuthFailure() throws Exception
    {
        UserDto user = userTestDataFactory.createUserWithCreds("chanakya2@gmail.com", "pass");


        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword());

        String token = "Bearer "+userTestDataFactory.loginAndGetJwtToken(authRequest)+"12";

        this.mockMvc.perform(get(String.format("/api/v1/resource/profile/%d/posts/0",
                userTestDataFactory.getThisUser("THIRD_USER").getId())).header("Authorization",token))
                .andExpect(status().isForbidden());



    }

}
