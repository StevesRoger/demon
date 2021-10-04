package org.jarvis.ws.medicine.exception;

import org.jarvis.core.exception.FatalException;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * Created: KimChheng
 * Date: 14-Nov-2020 Sat
 * Time: 3:57 PM
 */
public class StockMovementException extends FatalException {

    public StockMovementException() {
        super();
    }

    public StockMovementException(String message) {
        super(message);
    }

    public StockMovementException(String message, JResponseEntity response) {
        super(message, response);
    }

    public StockMovementException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockMovementException(Throwable cause) {
        super(cause);
    }

    public StockMovementException(Throwable cause, JResponseEntity responseFail) {
        super(cause, responseFail);
    }

    public StockMovementException(String message, String error) {
        super(message, error);
    }

    public StockMovementException(String message, String error, HttpStatus status) {
        super(message, error, status);
    }

    public StockMovementException(String message, String error, int status) {
        super(message, error, status);
    }
}
