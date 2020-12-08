package org.tx.statistics.api;

import org.tx.statistics.api.responses.ApiErrorResponse;
import org.tx.statistics.errors.EntityNotFoundError;
import org.tx.statistics.errors.InvalidAmountError;
import org.tx.statistics.errors.TimestampError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorHandler {

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception ex) {
        return ApiErrorResponse.createResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(Exception ex) {
        return ApiErrorResponse.createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundError.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex) {
        return ApiErrorResponse.createResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(value = {TimestampError.class})
    protected ResponseEntity<Object> handleValidationFailed(Exception ex) {
        return ApiErrorResponse.createResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(value = {InvalidAmountError.class})
    protected ResponseEntity<Object> handleInvalidAmount(Exception ex) {
        return ApiErrorResponse.createResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
}
