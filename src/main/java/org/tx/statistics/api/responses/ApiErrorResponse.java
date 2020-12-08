package org.tx.statistics.api.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiErrorResponse {

    private int responseCode;

    private String errorMessage;

    private Map<String, Object> details;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(int responseCode) {
        this(responseCode, null);
    }

    public ApiErrorResponse(int responseCode, String errorMessage) {
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public static ResponseEntity<Object> createResponseEntity(HttpStatus status) {
        return createResponseEntity(status, status.value(), null);
    }

    public static ResponseEntity<Object> createResponseEntity(HttpStatus status, String errorMessage) {
        return createResponseEntity(status, status.value(), errorMessage);
    }

    public static ResponseEntity<Object> createResponseEntity(HttpStatus status, int errorCode, String errorMessage) {
        return new ResponseEntity(new ApiErrorResponse(errorCode, errorMessage), status);
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getDetails() {
        return this.details;
    }

    public ApiErrorResponse setDetails(Map<String, Object> details) {
        this.details = details;
        return this;
    }

    public ApiErrorResponse addDetails(String key, Object value) {
        Map<String, Object> details = this.details == null ? new HashMap<>() : this.details;
        details.putIfAbsent(key, value);
        this.details = details;
        return this;
    }
}
