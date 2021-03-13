package com.springboot.rest.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboot.rest"))
                .paths(PathSelectors.regex("/api/v1.*"))
                .build()
                .apiInfo(getApiInfo());

    }

    public ApiInfo getApiInfo()
    {
        return new ApiInfoBuilder().title("Restful social network backend")
                .description("The application supports all the basic features of a social network such as Posts, Comments, Likes. " +
                        "Authorization is done using JWT's and resource privacy is provided for write operations.")
                .license("Open to use")
                .contact(new Contact("Chanakya Sunkarapally", "www.linkedin.com/in/chanakya-sunkarapally/", "sunkarapallychanakya@gmail.com"))
                .version("Version 1")
                .build();
    }
}
