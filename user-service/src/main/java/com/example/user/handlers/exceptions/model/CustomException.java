package com.example.user.handlers.exceptions.model;

import org.springframework.http.HttpStatus;
import java.util.List;


public class CustomException extends RuntimeException {
    private final String resource;
    private final HttpStatus status;
    private final List<String> validationErrors;


    public CustomException(String message, HttpStatus status, String resource, List<String> errors) {
        super(message);
        this.resource = resource;
        this.validationErrors = errors;
        this.status = status;
    }


    public CustomException(String message) {
        super(message);
        this.resource = null;
        this.validationErrors = null;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public String getResource() {
        return resource;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
