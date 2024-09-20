package com.example.freelance_project_management_platform.business.service.exceptions;

public class JWTException extends RuntimeException {

    public JWTException(String message) {
        super(message);
    }
}