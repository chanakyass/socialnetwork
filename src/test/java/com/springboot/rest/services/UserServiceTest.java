package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.DemoApplicationTests;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
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
public class UserServiceTest extends DemoApplicationTests {


    private final MockMvc mockMvc;
    private final UserTestDataFactory userTestDataFactory;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

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

    @Autowired
    public UserServiceTest(MockMvc mockMvc, UserTestDataFactory userTestDataFactory, UserMapper userMapper,
                           ObjectMapper objectMapper) {

        this.mockMvc = mockMvc;
        this.userTestDataFactory = userTestDataFactory;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }



    @Test
    public void testUserCreateSuccess() throws Exception  {

        User user = userTestDataFactory.createUserForTest();

            MvcResult createResult = this.mockMvc
                    .perform(post("/api/v1/public/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andReturn();
    }

    @Test
    public void testUserCreateFailure() throws Exception{

        User user = userTestDataFactory.createUserForTest();
        user.setEmail(null);
            MvcResult createResult = this.mockMvc.perform(post("/api/v1/public/register").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isInternalServerError())
                    .andReturn();


    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testUpdateSuccess() throws Exception{

            UserDto userEdit = new UserDto();

            userMapper.toUserEdit(userTestDataFactory.getLoggedInUser(), userEdit);
            userEdit.setAge(12);

            MvcResult createResult = this.mockMvc.perform(put("/api/v1/profile/my-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userEdit)))
                    .andExpect(status().isOk())
                    .andReturn();
    }


    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testUpdateFail() throws Exception{

        User user = userTestDataFactory.getOtherThanLoggedInUser();
        UserDto userEdit = new UserDto();
        userMapper.toUserEdit(user, userEdit);
        userEdit.setAge(13);

        MvcResult createResult = this.mockMvc.perform(put("/api/v1/profile/my-profile").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEdit)))
                .andExpect(status().isInternalServerError())
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testDeleteSuccess() throws Exception{
        User user = userTestDataFactory.getLoggedInUser();

        MvcResult createResult = this.mockMvc.perform(delete("/api/v1/profile/my-profile").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

    }
}
