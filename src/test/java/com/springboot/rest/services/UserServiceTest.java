package com.springboot.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.TestConfig;
import com.springboot.rest.config.security.SpringSecurityWebAuxTestConfig;
import com.springboot.rest.data.UserTestDataFactory;
import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.mapper.UserMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({TestBeansConfig.class, SpringSecurityWebAuxTestConfig.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {


    private final MockMvc mockMvc;
    private final UserTestDataFactory userTestDataFactory;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

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

        User user = userTestDataFactory.createUserForTest("CREATE_SUCCESS");

            MvcResult createResult = this.mockMvc
                    .perform(post("/api/v1/public/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andReturn();
    }

    @Test
    public void testUserCreateFailure() throws Exception{

        User user = userTestDataFactory.createUserForTest("CREATE_SUCCESS");
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
