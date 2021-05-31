package com.springboot.rest.service.integration.tests;

import com.springboot.rest.service.integration.data.CommentTestDataFactory;
import com.springboot.rest.service.integration.data.LikeTestDataFactory;
import com.springboot.rest.service.integration.data.PostTestDataFactory;
import com.springboot.rest.service.integration.data.UserTestDataFactory;
import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class GetServicesIT extends SocialNetworkApplicationIT {

    private final MockMvc mockMvc;
    private final CommentTestDataFactory commentTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final UserTestDataFactory userTestDataFactory;
    private final LikeTestDataFactory likeTestDataFactory;
    private final String uriPrefix;


    @Autowired
    public GetServicesIT(MockMvc mockMvc, CommentTestDataFactory commentTestDataFactory,
                         PostTestDataFactory postTestDataFactory, UserTestDataFactory userTestDataFactory,
                         LikeTestDataFactory likeTestDataFactory,
                         @Value("${app.uri.prefix}") String uriPrefix) {
        this.mockMvc = mockMvc;
        this.commentTestDataFactory = commentTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
        this.likeTestDataFactory = likeTestDataFactory;
        this.uriPrefix = uriPrefix;
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
                .perform(get(String.format(uriPrefix+"/resource/post/%d/comments?pageNo=0&adjustments=0", post.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataList.[*].id", Matchers.containsInAnyOrder(idsInInt)))
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
                .perform(get(String.format(uriPrefix+"/resource/comment/%d/replies?pageNo=0&adjustments=0", comment.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataList.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testGetPostsOfUserSuccess() throws Exception
    {
        List<PostDto> postsOfUser = postTestDataFactory.getPostsOfUser(userTestDataFactory.getThisUser("THIRD_USER"));
        Integer[] idsInInt = postsOfUser.stream().map(PostDto::getId).map(Long::intValue).toArray(Integer[]::new);
        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format(uriPrefix+"/resource/profile/%d/posts?pageNo=0&adjustments=0", userTestDataFactory.getThisUser("THIRD_USER").getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataList.[*].id", Matchers.containsInAnyOrder(idsInInt)))
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
                .perform(get(String.format(uriPrefix+"/resource/post/%d/likes", postDto.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataList.[*].id", Matchers.containsInAnyOrder(idsInInt)))
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
                .perform(get(String.format(uriPrefix+"/resource/comment/%d/likes", commentDto.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataList.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();


    }

    @Test
    public void testPostGetWithJwtAuthSuccess() throws Exception
    {
        UserDto user = userTestDataFactory.createUserWithCreds("chanakya@gmail.com", "pass");


        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword());

        String token = "Bearer "+userTestDataFactory.loginAndGetJwtToken(authRequest);

        this.mockMvc.perform(get(String.format(uriPrefix+"/resource/profile/%d/posts?pageNo=0&adjustments=0",
                userTestDataFactory.getThisUser("THIRD_USER").getId())).header("Authorization",token))
                .andExpect(status().isOk());



    }

    @Test
    public void testPostGetWithJwtAuthFailure() throws Exception
    {
        UserDto user = userTestDataFactory.createUserWithCreds("chanakya2@gmail.com", "pass");


        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword());

        String token = "Bearer "+userTestDataFactory.loginAndGetJwtToken(authRequest)+"12";

        this.mockMvc.perform(get(String.format(uriPrefix+"/resource/profile/%d/posts/0",
                userTestDataFactory.getThisUser("THIRD_USER").getId())).header("Authorization",token))
                .andExpect(status().isUnauthorized());



    }

}
