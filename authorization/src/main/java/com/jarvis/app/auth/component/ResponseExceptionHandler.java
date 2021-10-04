package com.jarvis.app.auth.component;

import com.jarvis.app.auth.controller.UserController;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.component.AbstractControllerAdviceHandler;
import com.jarvis.frmk.core.exception.base.AnyException;
import com.jarvis.frmk.core.exception.base.AnyRuntimeException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import com.jarvis.frmk.security.ISecurity;
import com.jarvis.frmk.security.exception.AnyAuthenticationException;
import com.jarvis.frmk.security.exception.UserAccountAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created: kim chheng
 * Date: 29-Sep-2019 Sun
 * Time: 1:33 PM
 */

@RestControllerAdvice(basePackageClasses = {UserController.class})
public class ResponseExceptionHandler extends AbstractControllerAdviceHandler {

    @LogSlf4j
    private LoggerJ log;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(Exception ex) {
        log.error(ex.getMessage(), ex);
        if (ex instanceof AnyRuntimeException)
            return ((AnyRuntimeException) ex).getResponse().code("E400").toResponseEntity();
        else if (ex instanceof AnyException)
            return ((AnyException) ex).getResponse().code("E400").toResponseEntity();
        else if (ex instanceof AnyAuthenticationException)
            return ((AnyAuthenticationException) ex).getResponse().code("E400").toResponseEntity();
        else if (ex instanceof ClientAuthenticationException)
            return handleInvalidAccessToken((OAuth2Exception) ex);
        else if (ex instanceof RuntimeException)
            return ResponseJEntity.fail(ex.getMessage(), HttpStatus.BAD_REQUEST, ex.getMessage()).code("E400").toResponseEntity();
        return ResponseJEntity.fail(I18N.getMessage("something.wrong"), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).code("E400").toResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        log.error(ex.getMessage());
        if (ex instanceof UserAccountAuthenticationException)
            return ((UserAccountAuthenticationException) ex).getResponse().toResponseEntity();
        return ResponseJEntity.fail(ISecurity.I18N_MESSAGE.getMessage("security.bad.credential"), HttpStatus.UNAUTHORIZED, ex.getMessage()).toResponseEntity();
    }

    protected ResponseEntity<?> handleInvalidAccessToken(OAuth2Exception ex) {
        String message = I18N.getMessage("user.bad.credential");
        String error = ex.getMessage();
        if (error.contains("expired"))
            message = I18N.getMessage("error.token.expired");
        else if (error.contains("Invalid refresh token"))
            message = I18N.getMessage("error.invalid.refresh.token");
        return buildResponse(ex.getMessage(), message, HttpStatus.UNAUTHORIZED).code("OA2400").toResponseEntity();
    }
}
