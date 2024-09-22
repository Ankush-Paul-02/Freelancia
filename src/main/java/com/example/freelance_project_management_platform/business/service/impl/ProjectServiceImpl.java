package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.dto.CreateProjectRequestDto;
import com.example.freelance_project_management_platform.business.dto.ProjectResponseDto;
import com.example.freelance_project_management_platform.business.service.AuthenticationService;
import com.example.freelance_project_management_platform.business.service.ProjectService;
import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import com.example.freelance_project_management_platform.data.enums.ProjectStatus;
import com.example.freelance_project_management_platform.data.enums.Role;
import com.example.freelance_project_management_platform.data.models.Project;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.data.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AuthenticationService authenticationService;

    @Override
    public ProjectResponseDto createProject(CreateProjectRequestDto createProjectRequestDto) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        authenticatedUser.getRoles().forEach(role -> {
            if (role.getName().equals(Role.FREELANCER)) {
                throw new UserInfoException("You are not authorized to create a project. Only clients can create projects");
            }
        });
        Project newProject = Project.builder()
                .title(createProjectRequestDto.getTitle())
                .description(createProjectRequestDto.getDescription())
                .client(authenticatedUser)
                .freelancer(null)
                .applications(new HashSet<>())
                .startDate(createProjectRequestDto.getStartDate())
                .endDate(createProjectRequestDto.getEndDate())
                .budget(createProjectRequestDto.getBudget())
                .projectStatus(ProjectStatus.PENDING)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();
        Project savedProject = projectRepository.save(newProject);
        return ProjectResponseDto.projectToProjectResponseDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getAllProjectByClient() {
        User currentUser = authenticationService.getAuthenticatedUser();
        return projectRepository.findAllByClient(currentUser).stream()
                .map(ProjectResponseDto::projectToProjectResponseDto)
                .toList();
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, String projectStatus) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        authenticatedUser.getRoles().forEach(role -> {
            if (role.getName().equals(Role.FREELANCER)) {
                throw new UserInfoException("You are not authorized to update a project status. Only clients can update projects status");
            }
        });
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            throw new UserInfoException("Project not found with id: " + projectId);
        }
        Project project = projectOptional.get();
        if (!project.getClient().getId().equals(authenticatedUser.getId())) {
            throw new UserInfoException("You are not authorized to update this project status");
        }
        if (projectStatus == null || projectStatus.isBlank()) {
            throw new UserInfoException("Project status is required");
        }

        if (ProjectStatus.valueOf(projectStatus) != ProjectStatus.COMPLETED || ProjectStatus.valueOf(projectStatus) != ProjectStatus.CANCELLED) {
            throw new UserInfoException("Invalid project status");
        }

        project.setProjectStatus(ProjectStatus.valueOf(projectStatus));
        project.setUpdatedAt(System.currentTimeMillis());
        Project updatedProject = projectRepository.save(project);
        return ProjectResponseDto.projectToProjectResponseDto(updatedProject);
    }
}
