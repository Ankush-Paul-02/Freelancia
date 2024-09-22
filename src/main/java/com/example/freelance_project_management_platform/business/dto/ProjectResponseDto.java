package com.example.freelance_project_management_platform.business.dto;

import com.example.freelance_project_management_platform.data.models.Project;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDto {

    private Long id;
    private String title;
    private String description;
    private UserDto client;
    private UserDto freelancer;
    private List<ApplicationDto> applications;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private String projectStatus;
    private Long createdAt;

    public static ProjectResponseDto projectToProjectResponseDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .client(UserDto.builder()
                        .name(project.getClient().getName())
                        .email(project.getClient().getEmail())
                        .build())
                .freelancer(project.getFreelancer() != null ? UserDto.builder()
                        .name(project.getFreelancer().getName())
                        .email(project.getFreelancer().getEmail())
                        .build() : null)
                .applications(project.getApplications().stream()
                        .map(application -> ApplicationDto.builder()
                                .freelancer(UserDto.builder()
                                        .name(application.getFreelancer().getName())
                                        .email(application.getFreelancer().getEmail())
                                        .build())
                                .coverLetter(application.getCoverLetter())
                                .appliedAt(application.getAppliedAt())
                                .isAccepted(application.getIsAccepted())
                                .build())
                        .toList())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .budget(project.getBudget())
                .projectStatus(project.getProjectStatus().name())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
