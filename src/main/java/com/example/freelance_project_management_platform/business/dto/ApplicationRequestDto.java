package com.example.freelance_project_management_platform.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationRequestDto {

    @NotBlank(message = "Project id is required")
    private Long projectId;

    @NotBlank(message = "Cover letter is required")
    private String coverLetter;
}
