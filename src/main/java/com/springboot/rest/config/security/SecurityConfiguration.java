package com.springboot.rest.config.security;

import com.springboot.rest.config.security.jwt.JwtTokenFilter;
import com.springboot.rest.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

// Here AuthenticationManagerBuilder builds a new AuthenticationManager for each user and user will be interacting with this authentication manager for the rest of the scenarios
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private AuthorizationService appUserDetailsService;
    private JwtTokenFilter jwtTokenFilter;
    private HandlerExceptionResolver exceptionHandler;

    public HandlerExceptionResolver getExceptionHandler() {
        return exceptionHandler;
    }

    @Autowired
    public void setExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthorizationService getAppUserDetailsService() {
        return appUserDetailsService;
    }

    @Autowired
    public void setAppUserDetailsService(AuthorizationService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    public JwtTokenFilter getJwtTokenFilter() {
        return jwtTokenFilter;
    }

    @Autowired
    public void setJwtTokenFilter(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http



                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            exceptionHandler.resolveException(request, response, null, ex);
                        }
                )
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> exceptionHandler.resolveException(httpServletRequest, httpServletResponse, null, e))
                .and().anonymous().and();
                http.authorizeRequests()
                //.antMatchers("api/v1/resource/**", "api/v1/profile/**")
                .antMatchers("/api/v1/public/**").permitAll()
                        .antMatchers("/api/v1/public").permitAll()
                        .antMatchers("/api/v1/public/*").permitAll()
                .anyRequest().authenticated();

                http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);



    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterRegistration(JwtTokenFilter filter) {
        FilterRegistrationBean<JwtTokenFilter> registration = new FilterRegistrationBean<JwtTokenFilter>(filter);
        registration.setEnabled(false);
        return registration;
    }
}

