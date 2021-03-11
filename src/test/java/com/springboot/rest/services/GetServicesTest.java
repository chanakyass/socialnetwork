package com.springboot.rest.services;

import com.springboot.rest.DemoApplicationTests;
import com.springboot.rest.data.CommentTestDataFactory;
import com.springboot.rest.data.PostTestDataFactory;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
@Slf4j
public class GetServicesTest extends DemoApplicationTests {

    private final MockMvc mockMvc;
    private final CommentTestDataFactory commentTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;
    private final UserTestDataFactory userTestDataFactory;


    @Autowired
    public GetServicesTest(MockMvc mockMvc, CommentTestDataFactory commentTestDataFactory,
                           PostTestDataFactory postTestDataFactory, UserTestDataFactory userTestDataFactory) {
        this.mockMvc = mockMvc;
        this.commentTestDataFactory = commentTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
        this.userTestDataFactory = userTestDataFactory;
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
    public void testGetCommentsOnPostSuccess() throws Exception
    {
        Post post = postTestDataFactory.getPreExistingPost();

        List<Comment> commentsOnPost = commentTestDataFactory.getAllCommentsOnPost(post);

        Integer[] idsInInt = commentsOnPost.stream().map((Comment comment)->comment.getId().intValue()).toArray(Integer[]::new);
        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/post/%d/comments/0", post.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();
    }

    @Test
    public void testGetRepliesOnCommentSuccess() throws Exception
    {
        Comment comment = commentTestDataFactory.getPreExistingComment();

        //get List of replies on comment
        List<Comment> repliesOnComment = commentTestDataFactory.getRepliesOnComment(comment);

        //map the comments list to an ids list

        Integer[] idsInInt = repliesOnComment.stream().map(Comment::getId).map(Long::intValue).toArray(Integer[]::new);

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
        List<Post> postsOfUser = postTestDataFactory.getPostsOfUser(userTestDataFactory.getUserWithNeed("THIRD_USER"));
        Integer[] idsInInt = postsOfUser.stream().map(Post::getId).map(Long::intValue).toArray(Integer[]::new);
        MvcResult mvcResult = this.mockMvc
                .perform(get(String.format("/api/v1/resource/profile/%d/posts/0", userTestDataFactory.getUserWithNeed("THIRD_USER").getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", Matchers.containsInAnyOrder(idsInInt)))
                .andReturn();
    }

}
