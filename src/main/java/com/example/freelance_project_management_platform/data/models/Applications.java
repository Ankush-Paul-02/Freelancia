package com.example.freelance_project_management_platform.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Applications extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", referencedColumnName = "id", nullable = false)
    private User freelancer;

    private String coverLetter;

    private LocalDateTime appliedAt;
    private Boolean isAccepted;
}
