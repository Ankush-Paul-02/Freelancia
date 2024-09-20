package com.example.freelance_project_management_platform.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DefaultResponseDto {


    private Status status;
    private Map data;
    private String message;

    public DefaultResponseDto(Status status, String message) {
        this(status, new HashMap<>(), message);
    }

    public enum Status {
        FAILED,
        SUCCESS
    }
}
