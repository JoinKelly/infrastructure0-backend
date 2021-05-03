package com.infrastructure.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomResponseStatusException extends ResponseStatusException {

    private String errorCode;
    public CustomResponseStatusException(HttpStatus status) {
        super(status);
    }

    public CustomResponseStatusException(HttpStatus status, String errorCode, String message) {
        super(status, message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
