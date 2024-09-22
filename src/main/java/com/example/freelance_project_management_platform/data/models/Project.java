package com.example.freelance_project_management_platform.data.models;

import com.example.freelance_project_management_platform.data.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private String title;
    private String description;

    //? Client who created the project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    //? Freelancer who is working on the project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", referencedColumnName = "id")
    private User freelancer;

    //? Applications who applied for the project
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Applications> applications = new HashSet<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private Long createdAt;
    private Long updatedAt;
}
