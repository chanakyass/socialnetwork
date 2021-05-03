package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.SocialNetworkApplicationTests;
import com.springboot.rest.data.PostTestDataFactory;
import com.springboot.rest.model.dto.post.PostDto;
import com.springboot.rest.model.dto.post.PostEditDto;
import com.springboot.rest.model.mapper.PostEditMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Slf4j
public class PostServiceTest extends SocialNetworkApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final PostEditMapper postEditMapper;
    private final PostTestDataFactory postTestDataFactory;
    private final String uriPrefix;


    @Autowired
    public PostServiceTest(MockMvc mockMvc, ObjectMapper objectMapper, PostEditMapper postEditMapper, PostTestDataFactory postTestDataFactory,
                            @Value("${app.uri.prefix}") String uriPrefix) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.postEditMapper = postEditMapper;
        this.postTestDataFactory = postTestDataFactory;
        this.uriPrefix = uriPrefix;
    }

    @BeforeAll
    void setup(@Autowired DataSource dataSource,
               @Autowired PlatformTransactionManager transactionManager) {
        try (Connection conn = dataSource.getConnection()) {
            // you'll have to make sure conn.autoCommit = true (default for e.g. H2)
            // e.g. url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;MODE=MySQL
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/write-related/1.sql"));
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
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/write-related/2.sql"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void testPostCreateSuccess() throws Exception
    {
        PostDto post = postTestDataFactory.createPostForLoggedInUser();

        MvcResult createResult = this.mockMvc
                .perform(post(uriPrefix+"/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testPostCreateFailureAuth() throws Exception
    {
        PostDto post = postTestDataFactory.createPostForOtherUser();
        MvcResult createResult = this.mockMvc
                .perform(post(uriPrefix+"/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    public void testPostCreateFailureOther() throws Exception
    {
        PostDto post = postTestDataFactory.createPostForLoggedInUser();
        post.setPostHeading(null);
        post.setPostBody(null);
        MvcResult createResult = this.mockMvc
                .perform(post(uriPrefix+"/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    public void testPostUpdateSuccess() throws Exception
    {
        // Create Post
        PostDto post = postTestDataFactory.getPreExistingPost();
        post.setPostBody("This is changed");

        PostEditDto postEdit = new PostEditDto();
        postEditMapper.toPostEditDto(post, postEdit);

        MvcResult createResult = this.mockMvc
                .perform(put(uriPrefix+"/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "chan@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testPostUpdateFailureAuth() throws Exception
    {
        PostEditDto postEdit = new PostEditDto();
        PostDto post = postTestDataFactory.getPreExistingPost();
        post.setPostBody("This is changed");
        postEditMapper.toPostEditDto(post, postEdit);

        MvcResult createResult = this.mockMvc
                .perform(put(uriPrefix+"/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    public void testPostDeleteSuccess() throws Exception
    {
        Long postId = postTestDataFactory.createPostForLoggedInUserAndInsertInDB();
        MvcResult createResult = this.mockMvc
                .perform(delete(uriPrefix+"/resource/post/"+postId))
                .andExpect(status().isOk())
                .andReturn();
    }

}
