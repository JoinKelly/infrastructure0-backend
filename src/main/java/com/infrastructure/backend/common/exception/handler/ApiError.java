package com.infrastructure.backend.common.exception.handler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class ApiError {

    private String error;
    private int status;
    private String message;
    private String path;
    private String timestamp;

    public ApiError(HttpServletRequest request) {
        this.path = request.getRequestURI();
        this.timestamp = new Date().toString();
    }

    public ApiError() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
