package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.DemoApplicationTests;
import com.springboot.rest.data.CommentTestDataFactory;
import com.springboot.rest.model.dto.CommentDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.mapper.CommentMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Slf4j
public class CommentServiceTest extends DemoApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CommentMapper commentMapper;
    private final CommentTestDataFactory commentTestDataFactory;


    @Autowired
    public CommentServiceTest(MockMvc mockMvc, ObjectMapper objectMapper, CommentMapper commentMapper,
                            CommentTestDataFactory commentTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.commentMapper = commentMapper;
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
        Comment comment = commentTestDataFactory.createCommentTemplateForLoggedInUser();
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
        Comment comment = commentTestDataFactory.createCommentTemplateForOtherUser();
        MvcResult createResult = this.mockMvc
                .perform(post("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    public void testCommentCreateFailureOther() throws Exception
    {
        Comment comment = commentTestDataFactory.createCommentTemplateForLoggedInUser();
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
        Comment comment = commentTestDataFactory.getPreExistingComment();
        comment.setCommentContent("This is changed");

        CommentDto commentEdit = new CommentDto();
        commentMapper.toCommentDto(comment, commentEdit);

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
        CommentDto commentEdit = new CommentDto();
        Comment comment = commentTestDataFactory.getPreExistingComment();
        comment.setCommentContent("This is changed");

        commentMapper.toCommentDto(comment, commentEdit);

        MvcResult createResult = this.mockMvc
                .perform(put("/api/v1/resource/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

}
