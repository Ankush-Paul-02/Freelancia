package com.example.freelance_project_management_platform.business.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateProjectRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Budget is required")
    private Double budget;

    @NotBlank(message = "Start date is required")
    private LocalDateTime startDate;

    @NotBlank(message = "End date is required")
    private LocalDateTime endDate;
}
