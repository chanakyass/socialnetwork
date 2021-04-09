package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.DemoApplicationTests;
import com.springboot.rest.data.LikeTestDataFactory;
import com.springboot.rest.model.dto.likes.LikeCommentDto;
import com.springboot.rest.model.dto.likes.LikePostDto;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Slf4j
public class LikeServiceTest extends DemoApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final LikeTestDataFactory likeTestDataFactory;

    @Autowired
    public LikeServiceTest(MockMvc mockMvc, ObjectMapper objectMapper, LikeTestDataFactory likeTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.likeTestDataFactory = likeTestDataFactory;
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
    @WithUserDetails(value = "whataview@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void likeAPostSuccess() throws Exception
    {
        LikePostDto like = likeTestDataFactory.createLikeOnPostForLoggedInUser();
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/post/1/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void UnlikePostSuccess() throws Exception
    {
        LikePostDto like = likeTestDataFactory.getPreExistingLikePost();
        MvcResult createResult = this.mockMvc
                .perform(delete("/api/v1/resource/post/1/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "whataview@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void likeACommentSuccess() throws Exception
    {
        LikeCommentDto like = likeTestDataFactory.createLikeOnCommentForLoggedInUser();
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/comment/1/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void UnlikeCommentSuccess() throws Exception
    {
        LikeCommentDto like = likeTestDataFactory.getPreExistingLikeComment();
        MvcResult createResult = this.mockMvc
                .perform(delete("/api/v1/resource/comment/1/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andReturn();
    }
}
