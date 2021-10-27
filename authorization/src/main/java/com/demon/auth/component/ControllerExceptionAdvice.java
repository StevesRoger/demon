package com.demon.auth.component;

import com.demon.auth.controller.UserController;
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

/**
 * Created: kim chheng
 * Date: 29-Sep-2019 Sun
 * Time: 1:33 PM
 */

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        if (ex instanceof RuntimeException)
            return ((AnyRuntimeException) ex).getResponse().code("E400").toResponseEntity();
        else if (ex instanceof AnyException)
            return ((AnyException) ex).getResponse().code("E400").toResponseEntity();
        else if (ex instanceof ClientAuthenticationException)
            return handleInvalidAccessToken((OAuth2Exception) ex);
        else if (ex instanceof RuntimeException)
            return ResponseJEntity.fail(ex.getMessage(), HttpStatus.BAD_REQUEST, ex.getMessage()).code("E400").toResponseEntity();
        return ResponseJEntity.fail(I18N.getMessage("something.wrong"), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).code("E400").toResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        LOG.error(ex.getMessage());
        if (ex instanceof UserAccountException)
            return ((UserAccountException) ex).getResponse().toResponseEntity();
        return ResponseJEntity.fail(ISecurity.I18N_MESSAGE.getMessage("security.bad.credential"), HttpStatus.UNAUTHORIZED, ex.getMessage()).toResponseEntity();
    }

    protected ResponseEntity<?> handleInvalidAccessToken(OAuth2Exception ex) {
        String message = I18N.getMessage("user.bad.credential");
        String error = ex.getMessage();
        if (error.contains("expired"))
            message = I18N.getMessage("error.token.expired");
        else if (error.contains("Invalid refresh token"))
            message = I18N.getMessage("error.invalid.refresh.token");
        else if (error.contains("User account is locked"))
            message = I18N.getMessage("error.user.locked");
        return buildResponse(ex.getMessage(), message, HttpStatus.UNAUTHORIZED).code("OA2400").toResponseEntity();
    }
}
