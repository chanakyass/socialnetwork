package com.springboot.rest.config.exceptions;

import com.springboot.rest.config.error.ApiCallError;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class GlobalExceptionHandler{

    private final Logger logger = LogManager.getLogger();
    private final PermissionEvaluator permissionEvaluator;
    private final ApplicationContext applicationContext;

    public GlobalExceptionHandler(PermissionEvaluator permissionEvaluator, ApplicationContext applicationContext) {
        this.permissionEvaluator = permissionEvaluator;
        this.applicationContext = applicationContext;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiCallError<String>> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        logger.error("NotFoundException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiCallError<>(request.getRequestURI(), "Not found exception", LocalDateTime.now(), List.of(ex.getMessage())));

    }

    @ExceptionHandler({AccessDeniedException.class, ApiAccessException.class})
    public ResponseEntity<ApiCallError<String>> handleAccessDeniedException(HttpServletRequest request, Exception ex) {
        logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);

        if (request.isUserInRole("ROLE_USER")) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiCallError<>(request.getRequestURI(), "Access denied! Please login as user.", LocalDateTime.now(), List.of(ex.getMessage())));
        } else if (request.isUserInRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiCallError<>(request.getRequestURI(), "Access denied! Please login as admin.", LocalDateTime.now(), List.of(ex.getMessage())));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiCallError<>(request.getRequestURI(), "Issue with user session.", LocalDateTime.now(), List.of(ex.getMessage())));


    }

    @ExceptionHandler(ApiSpecificException.class)
    public ResponseEntity<ApiCallError<String>> handleApiSpecificException(HttpServletRequest request, ApiSpecificException ex) {
        logger.error("handleApiSpecificException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiCallError<>(request.getRequestURI(), "Error in processing!", LocalDateTime.now(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiCallError<String>> handleInternalServerError(HttpServletRequest request, Exception ex) {
        logger.error("handleInternalServerError {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiCallError<>(request.getRequestURI(), "Internal server error", LocalDateTime.now(), List.of(ex.getMessage())));
    }

}
