package com.demo.product.component;

import com.demo.product.controller.ProductController;
import com.demo.product.domain.response.ResponseBody;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = {ProductController.class})
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleAnyException(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        if (ex instanceof AuthenticationException)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBody.fail("E401", "Unauthorized access"));
        else if (ex instanceof AccessDeniedException)
            return handleAccessDeniedException((AccessDeniedException) ex);
        else if (ex instanceof RestClientResponseException) {
            RestClientResponseException e = (RestClientResponseException) ex;
            return ResponseEntity.status(e.getRawStatusCode()).body(new JSONObject(e.getResponseBodyAsString()).toMap());
        } else if (ex instanceof RuntimeException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.fail("E400", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBody.fail("E500", "Unexpected error"));
    }

    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        String error = ex.getMessage();
        String message = "Forbidden";
        if (error.contains("Insufficient scope"))
            message = "Client has no scope to access this resource";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBody.fail("E401", message));
    }
}
