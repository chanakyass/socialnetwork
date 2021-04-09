package com.springboot.rest.config.exceptions;

import com.springboot.rest.config.error.ApiCallError;
import com.springboot.rest.model.dto.response.Error;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler{

    private final Logger logger = LogManager.getLogger();

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error<ApiCallError>> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        logger.error("NotFoundException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error<>(new ApiCallError(request.getRequestURI(), "Not found exception", LocalDateTime.now(), List.of(ex.getMessage()))));

    }

    @ExceptionHandler({AccessDeniedException.class, ApiAccessException.class})
    public ResponseEntity<Error<ApiCallError>> handleAccessDeniedException(HttpServletRequest request, Exception ex) {
        logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);

        if (request.isUserInRole("ROLE_USER")) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error<>(new ApiCallError(request.getRequestURI(), "Access denied! Please login as user.", LocalDateTime.now(), List.of(ex.getMessage()))));
        } else if (request.isUserInRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error<>(new ApiCallError(request.getRequestURI(), "Access denied! Please login as admin.", LocalDateTime.now(), List.of(ex.getMessage()))));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Error<>(new ApiCallError(request.getRequestURI(), "Issue with user session.", LocalDateTime.now(), List.of(ex.getMessage()))));


    }

    @ExceptionHandler(ApiSpecificException.class)
    public ResponseEntity<Error<ApiCallError>> handleApiSpecificException(HttpServletRequest request, ApiSpecificException ex) {
        logger.error("handleApiSpecificException {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error<>(new ApiCallError(request.getRequestURI(), "Error in processing!", LocalDateTime.now(), List.of(ex.getMessage()))));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error<ApiCallError>> handleInternalServerError(HttpServletRequest request, Exception ex) {
        logger.error("handleInternalServerError {}\n", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error<>(new ApiCallError(request.getRequestURI(), "Internal server error", LocalDateTime.now(), List.of(ex.getMessage()))));
    }

}
