package com.springboot.rest.integration.tests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@TestConfiguration
public class TestConfig {

    @Bean
    public ObjectMapper getObjectMapper()
    {
        return new ObjectMapper();
    }

    @Bean
    public static Random getRandom()
    {
        return new Random();
    }

}
