package com.springboot.rest;


import com.springboot.rest.config.TestBeansConfig;
import com.springboot.rest.config.TestConfig;
import com.springboot.rest.config.security.SpringSecurityWebAuxTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestBeansConfig.class, SpringSecurityWebAuxTestConfig.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

}
