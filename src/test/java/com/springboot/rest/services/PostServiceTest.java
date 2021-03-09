package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.TestConfig;
import com.springboot.rest.config.security.SpringSecurityWebAuxTestConfig;
import com.springboot.rest.data.PostTestDataFactory;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.dto.PostDto;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Import({TestBeansConfig.class, SpringSecurityWebAuxTestConfig.class, TestConfig.class})
@Slf4j
public class PostServiceTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final PostMapper postMapper;
    private final UserTestDataFactory userTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;


    @Autowired
    public PostServiceTest(MockMvc mockMvc, ObjectMapper objectMapper, PostMapper postMapper,
                           UserTestDataFactory userTestDataFactory, PostTestDataFactory postTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.postMapper = postMapper;
        this.userTestDataFactory = userTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
    }

    @BeforeAll

        void setup(@Autowired DataSource dataSource,
            @Autowired PlatformTransactionManager transactionManager) {
        new TransactionTemplate(transactionManager).execute((ts) -> {
            try (Connection conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("1.sql"));
                //ScriptUtils.executeSqlScript(conn, new ClassPathResource("album.sql"));
                // should work without manually commit but didn't for me (because of using AUTOCOMMIT=OFF)
                // I use url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;MODE=MySQL;AUTOCOMMIT=OFF
                // same will happen with DataSourceInitializer & DatabasePopulator (at least with this setup)
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }


    @AfterAll
    public void fireDown(@Autowired DataSource dataSource,
                         @Autowired PlatformTransactionManager transactionManager)
    {
        new TransactionTemplate(transactionManager).execute((ts) -> {
            try (Connection conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("2.sql"));
                //ScriptUtils.executeSqlScript(conn, new ClassPathResource("album.sql"));
                // should work without manually commit but didn't for me (because of using AUTOCOMMIT=OFF)
                // I use url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;MODE=MySQL;AUTOCOMMIT=OFF
                // same will happen with DataSourceInitializer & DatabasePopulator (at least with this setup)
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Test
    public void testPostCreateSuccess() throws Exception
    {
        Post post = postTestDataFactory.createPostForLoggedInUser("POST_SUCCESS");

        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testPostCreateFailureAuth() throws Exception
    {
        Post post = postTestDataFactory.createPostForOtherUser("POST_SUCCESS");
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    public void testPostCreateFailureOther() throws Exception
    {
        Post post = postTestDataFactory.createPostForLoggedInUser("POST_SUCCESS");
        post.setPostHeading(null);
        post.setPostBody(null);
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    public void testPostUpdateSuccess() throws Exception
    {
        // Create Post
        Post post = postTestDataFactory.getPreExistingPost();
        post.setPostBody("This is changed");

        PostDto postEdit = new PostDto();
        postMapper.toPostDto(post, postEdit);

        MvcResult createResult = this.mockMvc
                .perform(put("/api/v1/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "chan@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testPostUpdateFailureAuth() throws Exception
    {
        PostDto postEdit = new PostDto();
        Post post = postTestDataFactory.getPreExistingPost();
        post.setPostBody("This is changed");
        postMapper.toPostDto(post, postEdit);

        MvcResult createResult = this.mockMvc
                .perform(put("/api/v1/resource/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

}
