package com.example.freelance_project_management_platform.business.service.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InvalidInputException extends RuntimeException {

    private Map data = new HashMap();

    public InvalidInputException() {
        super("Invalid input");
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Map data) {
        super(message);
        this.data = data;
    }
}