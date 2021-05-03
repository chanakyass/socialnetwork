package com.springboot.rest.config.security;

import com.springboot.rest.config.security.jwt.JwtTokenFilter;
import com.springboot.rest.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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

// Here AuthenticationManagerBuilder builds a new AuthenticationManager for each user and user will be interacting with this authentication manager for the rest of the scenarios
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthorizationService appUserDetailsService;
    private final JwtTokenFilter jwtTokenFilter;
    private final String uriPrefix;

    @Autowired
    public SecurityConfiguration(@Lazy AuthorizationService appUserDetailsService, JwtTokenFilter jwtTokenFilter, @Value("${app.uri.prefix}") String uriPrefix){
        this.appUserDetailsService = appUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.uriPrefix = uriPrefix;
    }


    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
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
                .and().anonymous().and()
                .authorizeRequests()
                //.antMatchers("api/v1/resource/**", "api/v1/profile/**")
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(uriPrefix+"/public/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/configuration/security").permitAll()
                .antMatchers("/swagger-resources/**", "/webjars/**", "/swagger-ui.html", "/swagger-ui.html/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
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
        FilterRegistrationBean<JwtTokenFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}

