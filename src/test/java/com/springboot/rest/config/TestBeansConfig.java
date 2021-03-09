package com.springboot.rest.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@TestConfiguration
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestBeansConfig {

    @Bean(name = "otherUser")
    public static User getOtherUserInContext()
    {
        return getTestUsersMap().get("SUCCESS2");
    }


    @Bean(name = "testUsers")
    public static HashMap<String, User> getTestUsersMap(/*@Value("${springboot.test.config.path}"*/)
    {
        String pathToTestUsers="src/test/resources/myfile.json";
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, User> userHashMap = null;

        try {

            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(new FileReader(pathToTestUsers));
            TypeReference<HashMap<String, User>> typeRef
                    = new TypeReference<>() {
            };
            userHashMap = objectMapper.readValue(jsonParser, typeRef);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return userHashMap;
    }


    @Bean(name = "testPosts")
    public static HashMap<String, Post> getTestPostsMap(/*@Value("${springboot.test.config.path}")*/)
    {
        String pathToTestPosts="src/test/resources/resources.json";
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        HashMap<String, Post> postsHashMap = null;

        try {

            DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ISO_LOCAL_DATE;
            JsonFactory jsonFactory = new JsonFactory();
            FileReader fileReader = new FileReader(pathToTestPosts);
            JsonParser jsonParser = jsonFactory.createParser(fileReader);
            String str =jsonParser.getValueAsString();
            System.out.println(str);
            TypeReference<HashMap<String, Post>> typeRef
                    = new TypeReference<>() {
            };
            postsHashMap = objectMapper.readValue(jsonParser, typeRef);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return postsHashMap;
    }
}

