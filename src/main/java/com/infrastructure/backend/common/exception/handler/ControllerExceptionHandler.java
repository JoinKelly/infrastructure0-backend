package com.infrastructure.backend.common.exception.handler;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiError> exception(HttpServletRequest req, Exception e) {
        logger.error(e.getMessage(), e);
        ApiError error = new ApiError(req);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError("INTERNAL_SERVER_ERROR");
        error.setMessage(e.getMessage());
        ResponseEntity response = new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return response;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ApiError> supportedMethodException(HttpServletRequest req,
                                                             Exception ex) {

        ApiError error = new ApiError(req);
        error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        error.setError("METHOD_NOT_ALLOWED");
        error.setMessage(ex.getMessage());
        ResponseEntity response = new ResponseEntity<>(
                error,
                HttpStatus.METHOD_NOT_ALLOWED
        );
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(HttpServletRequest req,
                                                               MethodArgumentNotValidException ex) {
        ApiError error = new ApiError(req);
        error.setStatus(HttpStatus.BAD_REQUEST.value());

        ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
        error.setError("BAD_REQUEST");
        error.setMessage(objectError.getDefaultMessage());
        ResponseEntity response = new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
        return response;

    }

    @ExceptionHandler(CustomResponseStatusException.class)
    @ResponseBody
    public ResponseEntity handleMyGovernanceException(HttpServletRequest req, CustomResponseStatusException ex) {
        ApiError error = new ApiError(req);
        error.setStatus(ex.getStatus().value());
        error.setError(ex.getErrorCode());
        error.setMessage(ex.getReason());
        ResponseEntity response = new ResponseEntity<>(
                error,
                ex.getStatus()
        );
        return response;
    }

}
