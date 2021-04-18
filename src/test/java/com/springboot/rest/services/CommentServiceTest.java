package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.SocialNetworkApplicationTests;
import com.springboot.rest.data.CommentTestDataFactory;
import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.mapper.CommentEditMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Slf4j
public class CommentServiceTest extends SocialNetworkApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CommentEditMapper commentEditMapper;
    private final CommentTestDataFactory commentTestDataFactory;


    @Autowired
    public CommentServiceTest(MockMvc mockMvc, ObjectMapper objectMapper, CommentEditMapper commentEditMapper,
                            CommentTestDataFactory commentTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.commentEditMapper = commentEditMapper;
        this.commentTestDataFactory = commentTestDataFactory;
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
    public void testCommentCreateSuccess() throws Exception
    {
        CommentDto comment = commentTestDataFactory.createCommentTemplateForLoggedInUser();
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testCommentCreateFailureAuth() throws Exception
    {
        CommentDto comment = commentTestDataFactory.createCommentTemplateForOtherUser();
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    public void testCommentCreateFailureOther() throws Exception
    {
        CommentDto comment = commentTestDataFactory.createCommentTemplateForLoggedInUser();
        comment.setCommentContent(null);
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    public void testCommentUpdateSuccess() throws Exception
    {
        // Create Comment
        CommentDto comment = commentTestDataFactory.getPreExistingComment();
        comment.setCommentContent("This is changed");

        CommentEditDto commentEdit = new CommentEditDto();
        commentEditMapper.toCommentEditDto(comment, commentEdit);

        MvcResult createResult = this.mockMvc
                .perform(put("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "chan@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testCommentUpdateFailureAuth() throws Exception
    {
        CommentEditDto commentEdit = new CommentEditDto();
        CommentDto comment = commentTestDataFactory.getPreExistingComment();
        comment.setCommentContent("This is changed");

        commentEditMapper.toCommentEditDto(comment, commentEdit);

        MvcResult createResult = this.mockMvc
                .perform(put("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    public void testCommentDeleteSuccess() throws Exception
    {
        Long commentId = commentTestDataFactory.createCommentTemplateForLoggedInUserAndInsertInDb();
        MvcResult createResult = this.mockMvc
                .perform(delete("/api/v1/resource/comment/"+commentId))
                .andExpect(status().isOk())
                .andReturn();
    }

}
