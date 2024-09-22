package com.example.freelance_project_management_platform.business.dto;

import com.example.freelance_project_management_platform.data.models.Applications;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private Long id;
    private UserDto freelancer;
    private String coverLetter;
    private LocalDateTime appliedAt;
    private Boolean isAccepted;

    public static ApplicationDto applicationToApplicationDto(Applications application) {
        return ApplicationDto.builder()
                .id(application.getId())
                .freelancer(
                        UserDto.builder()
                                .name(application.getFreelancer().getName())
                                .email(application.getFreelancer().getEmail())
                                .build()
                )
                .coverLetter(application.getCoverLetter())
                .appliedAt(application.getAppliedAt())
                .isAccepted(application.getIsAccepted())
                .build();
    }
}
