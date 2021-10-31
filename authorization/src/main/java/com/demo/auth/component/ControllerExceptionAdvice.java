package com.demo.auth.component;

import com.demo.auth.controller.UserController;
import com.demo.auth.domain.response.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        if (ex instanceof ClientAuthenticationException)
            return handleInvalidAccessToken((OAuth2Exception) ex);
        else if (ex instanceof AuthenticationException)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBody.fail("E401", ex.getMessage()));
        else if (ex instanceof RuntimeException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.fail("E400", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBody.fail("E500", "Unexpected error"));
    }

    protected ResponseEntity<?> handleInvalidAccessToken(OAuth2Exception ex) {
        String message = "Incorrect username or password";
        String error = ex.getMessage();
        if (error.contains("expired"))
            message = "Token has expired";
        else if (error.contains("Invalid refresh token"))
            message = "Invalid refresh token";
        else if (error.contains("User account is locked"))
            message = "User account is locked";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBody.fail("E401", message));
    }
}
