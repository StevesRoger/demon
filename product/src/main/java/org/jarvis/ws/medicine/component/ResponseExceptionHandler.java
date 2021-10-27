package org.jarvis.ws.medicine.component;

import org.apache.ibatis.binding.BindingException;
import org.jarvis.core.I18N;
import org.jarvis.core.annotation.InjectLogSuffix;
import org.jarvis.core.component.AbstractControllerExceptionHandler;
import org.jarvis.core.exception.base.AnyException;
import org.jarvis.core.exception.base.AnyRuntimeException;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.JsonUtil;
import org.jarvis.ws.medicine.controller.MedicineController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackageClasses = {MedicineController.class})
public class ResponseExceptionHandler extends AbstractControllerExceptionHandler {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @InjectLogSuffix
    private LogSuffix log;

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<?> handleAnyException(Exception ex, WebRequest request) throws Exception {
        logger.error(ex.getMessage(), ex);
        if (ex instanceof AccessDeniedException) {
            String error = ex.getMessage();
            String message = "Forbidden";
            if (error.contains("Insufficient scope"))
                message = I18N.getMessage("security.client.no.scope");
            return JResponseEntity.fail(message, HttpStatus.FORBIDDEN).error(error).toResponseEntity();
        } else if (RestClientResponseException.class.isAssignableFrom(ex.getClass())) {
            RestClientResponseException restEx = (RestClientResponseException) ex;
            return ResponseEntity.status(restEx.getRawStatusCode()).body(JsonUtil.toMap(restEx.getResponseBodyAsString()));
        } else if (AnyRuntimeException.class.isAssignableFrom(ex.getClass()))
            return ((AnyRuntimeException) ex).getResponse().toResponseEntity();
        else if (AnyException.class.isAssignableFrom(ex.getClass()))
            return ((AnyException) ex).getResponse().toResponseEntity();
        else if (DataAccessException.class.isAssignableFrom(ex.getClass()) || BindingException.class.isAssignableFrom(ex.getClass()))
            return JResponseEntity.fail(I18N.getMessage("data.access.error"), HttpStatus.EXPECTATION_FAILED).error(ex.getMessage()).toResponseEntity();
        else if (AuthenticationException.class.isAssignableFrom(ex.getClass()))
            return new JResponseEntity(I18N.getMessage("security.user.unauthorized"), HttpStatus.UNAUTHORIZED).error(ex.getMessage()).toResponseEntity();
        return JResponseEntity.fail(I18N.getMessage("something.wrong"), HttpStatus.INTERNAL_SERVER_ERROR).error(ex.getMessage()).toResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleExceptionInternal(ex, body, headers, status, request);
        String message = activeProfile.equals("dev") ? ex.getMessage() : I18N.getMessage("unexpected.error");
        if (ex instanceof MethodArgumentNotValidException)
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, headers, status, request);
        return new ResponseEntity<>(body == null ?
                buildResponse(ex.getMessage(), message, status) : body, headers, status);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = ex.getMessage();
        if (fieldError != null)
            message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        return new ResponseEntity<>(buildResponse(ex.getMessage(), message, status), headers, status);
    }
}
