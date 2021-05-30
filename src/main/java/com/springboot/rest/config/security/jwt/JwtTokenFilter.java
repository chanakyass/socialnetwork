package com.springboot.rest.config.security.jwt;

import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.config.exceptions.ApiSpecificException;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.entities.UserAdapter;
import com.springboot.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private HandlerExceptionResolver exceptionHandler;

    public HandlerExceptionResolver getExceptionHandler() {
        return exceptionHandler;
    }

    @Autowired
    public void setExceptionHandler( @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public JwtTokenUtil getJwtTokenUtil() {
        return jwtTokenUtil;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {

            String token = jwtTokenUtil.extractToken(request);
            if (token == null) {
                chain.doFilter(request, response);
                return;
            }
            if (!jwtTokenUtil.validate(token)) {

                throw new ApiAccessException("Unauthorized");

            }

            // Get user identity and set it on the spring security context
            String payload = jwtTokenUtil.getSubjectFromToken(token);
            Long userId = Long.parseLong(payload.trim().split(":")[0]);
            String email = payload.trim().split(":")[1];

            User user = userService.getUserByIdAndEmail(userId, email);

            UserDetails userDetails = new UserAdapter(user);

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        catch(ApiSpecificException ex){
            exceptionHandler.resolveException(request, response, null,
                    new ApiAccessException(ex.getMessage()));
        }
        catch (Exception e)
        {
            exceptionHandler.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return (request.getRequestURI().contains("public"));

    }
}
