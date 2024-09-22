package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.dto.CreateProjectRequestDto;
import com.example.freelance_project_management_platform.business.dto.ProjectResponseDto;
import com.example.freelance_project_management_platform.business.service.AuthenticationService;
import com.example.freelance_project_management_platform.business.service.ProjectService;
import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import com.example.freelance_project_management_platform.data.enums.ProjectStatus;
import com.example.freelance_project_management_platform.data.enums.Role;
import com.example.freelance_project_management_platform.data.models.Project;
import com.example.freelance_project_management_platform.data.models.Roles;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.data.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
