package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.SocialNetworkApplicationTests;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.dto.auth.AuthRequest;
import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserEditDto;
import com.springboot.rest.model.mapper.UserEditMapper;
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
public class UserServiceTest extends SocialNetworkApplicationTests {


    private final MockMvc mockMvc;
    private final UserTestDataFactory userTestDataFactory;
    private final ObjectMapper objectMapper;
    private final UserEditMapper userEditMapper;
    private final String uriPrefix;

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
    public UserServiceTest(MockMvc mockMvc, UserTestDataFactory userTestDataFactory, UserEditMapper userEditMapper,
                           ObjectMapper objectMapper, @Value("${app.uri.prefix}") String uriPrefix) {

        this.mockMvc = mockMvc;
        this.userTestDataFactory = userTestDataFactory;
        this.objectMapper = objectMapper;
        this.userEditMapper = userEditMapper;
        this.uriPrefix = uriPrefix;
    }



    @Test
    public void testUserCreateSuccess() throws Exception  {

        UserDto user = userTestDataFactory.createUserForTest();

            MvcResult createResult = this.mockMvc
                    .perform(post(uriPrefix+"/public/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user).replaceAll("}$", ",\"password\": \"pass\"}")))
                    .andExpect(status().isOk())
                    .andReturn();
    }

    @Test
    public void testUserCreateFailure() throws Exception{

        UserDto user = userTestDataFactory.createUserForTest();
        user.setEmail(null);
            MvcResult createResult = this.mockMvc.perform(post(uriPrefix+"/public/register").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isInternalServerError())
                    .andReturn();


    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testUpdateSuccess() throws Exception{

            UserEditDto userEdit = new UserEditDto();

            userEditMapper.toUserEdit(userTestDataFactory.getLoggedInUser(), userEdit);
            userEdit.setUserSummary("blah");

            MvcResult createResult = this.mockMvc.perform(put(uriPrefix+"/profile/my-profile").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userEdit)))
                    .andExpect(status().isOk())
                    .andReturn();
    }


    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testUpdateFail() throws Exception{

        UserDto user = userTestDataFactory.getOtherThanLoggedInUser();

        UserEditDto userEdit = new UserEditDto();
        userEditMapper.toUserEdit(user, userEdit);
        userEdit.setUserSummary("blah");

        MvcResult createResult = this.mockMvc.perform(put(uriPrefix+"/profile/my-profile").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEdit)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    @WithUserDetails(value = "test@rest.com", userDetailsServiceBeanName = "basicUsers")
    public void testDeleteSuccess() throws Exception{
        UserDto user = userTestDataFactory.getLoggedInUser();

        MvcResult createResult = this.mockMvc.perform(delete(uriPrefix+"/profile/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testAuthenticationFailure() throws Exception
    {
        UserDto user = userTestDataFactory.createUserWithCreds("chanakya2@gmail.com", "pass");


        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword()+"12");

        MvcResult createResult = this.mockMvc.perform(post(uriPrefix+"/public/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andReturn();

    }
}
