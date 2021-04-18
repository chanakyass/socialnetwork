package com.springboot.rest.config.exceptions;

import com.springboot.rest.config.error.ApiCallError;
import com.springboot.rest.model.dto.response.Error;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler{

    private final Logger logger = LogManager.getLogger();

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Error<ApiCallError>> handleNotFoundException(HttpServletRequest request, NoHandlerFoundException ex) {
        logger.error("NotFoundException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error<>(new ApiCallError(HttpStatus.NOT_FOUND.value(),request.getRequestURI(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage()))));

    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Error<ApiCallError>> handleAuthenticationException(HttpServletRequest request, AuthenticationException ex) {
        logger.error("handleAuthenticationException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new Error<>(new ApiCallError(HttpStatus.UNAUTHORIZED.value(), request.getRequestURI(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage() ))));
    }

    @ExceptionHandler({ ApiAccessException.class})
    public ResponseEntity<Error<ApiCallError>> handleAccessDeniedException(HttpServletRequest request, Exception ex)   {
        logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Error<>((new ApiCallError(HttpStatus.UNAUTHORIZED.value(), request.getRequestURI(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage())))));

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error<ApiCallError>> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex){
        logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);
        if (request.isUserInRole("ROLE_USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error<>(new ApiCallError(HttpStatus.FORBIDDEN.value(), request.getRequestURI(),
                            HttpStatus.FORBIDDEN.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage() ))));
        } else if (request.isUserInRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error<>(new ApiCallError(HttpStatus.FORBIDDEN.value(), request.getRequestURI(),
                            HttpStatus.FORBIDDEN.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage() ))));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Error<>(new ApiCallError(HttpStatus.FORBIDDEN.value(), request.getRequestURI(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage() ))));

    }

    @ExceptionHandler(ApiSpecificException.class)
    public ResponseEntity<Error<ApiCallError>> handleApiSpecificException(HttpServletRequest request, ApiSpecificException ex) {
        logger.error("handleApiSpecificException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error<>(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI(), ExceptionType.API_SPECIFIC_EXCEPTION, "Error in processing!", LocalDateTime.now(), List.of(ex.getMessage()))));
    }

    @ExceptionHandler(ApiResourceNotFoundException.class)
    public ResponseEntity<Error<ApiCallError>> handleApiResourceNotFoundException(HttpServletRequest request, ApiResourceNotFoundException ex) {
        logger.error("handleApiResourceNotFoundException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error<>(new ApiCallError(HttpStatus.NOT_FOUND.value(),
                        request.getRequestURI(), ExceptionType.API_RESOURCE_NOT_FOUND_EXCEPTION, "Resource may have been deleted", LocalDateTime.now(), List.of(ex.getMessage()))));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error<ApiCallError>> handleInternalServerError(HttpServletRequest request, Exception ex) {
        logger.error("handleInternalServerError {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error<>(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), LocalDateTime.now(), List.of(ex.getMessage()))));
    }

}
