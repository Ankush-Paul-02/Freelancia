package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.dto.ApplicationDto;
import com.example.freelance_project_management_platform.business.dto.ApplicationRequestDto;
import com.example.freelance_project_management_platform.business.service.ApplicationService;
import com.example.freelance_project_management_platform.business.service.AuthenticationService;
import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import com.example.freelance_project_management_platform.data.enums.Role;
import com.example.freelance_project_management_platform.data.models.Applications;
import com.example.freelance_project_management_platform.data.models.Project;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.data.repositories.ApplicationsRepository;
import com.example.freelance_project_management_platform.data.repositories.ProjectRepository;
import com.example.freelance_project_management_platform.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationsRepository applicationsRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AuthenticationService authenticationService;

    @Override
    public ApplicationDto submitApplication(ApplicationRequestDto applicationRequestDto) {
        User currentFreelancer = authenticationService.getAuthenticatedUser();
        currentFreelancer.getRoles().forEach(role -> {
            if (role.getName().equals(Role.CLIENT)) {
                throw new UserInfoException("You are not authorized to submit an application. Only freelancers can submit applications");
            }
        });

        Optional<Project> optionalProject = projectRepository.findById(applicationRequestDto.getProjectId());
        if (optionalProject.isEmpty()) {
            throw new UserInfoException("Project not found with id: " + applicationRequestDto.getProjectId());
        }

        Applications applications = Applications.builder()
                .freelancer(currentFreelancer)
                .project(optionalProject.get())
                .appliedAt(LocalDateTime.now())
                .coverLetter(applicationRequestDto.getCoverLetter())
                .isAccepted(false)
                .build();
        Applications savedApplication = applicationsRepository.save(applications);
        return ApplicationDto.applicationToApplicationDto(savedApplication);
    }

    @Override
    public List<ApplicationDto> getApplicationsByProjectId(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new UserInfoException("Project not found with id: " + projectId);
        }
        List<Applications> applications = applicationsRepository.findAllByProject(optionalProject.get());
        return applications.stream().map(ApplicationDto::applicationToApplicationDto).toList();
    }

    @Override
    public ApplicationDto acceptApplication(Long applicationId) {
        User currentClient = authenticationService.getAuthenticatedUser();
        currentClient.getRoles().forEach(role -> {
            if (role.getName().equals(Role.FREELANCER)) {
                throw new UserInfoException("You are not authorized to accept an application. Only clients can accept applications");
            }
        });

        Optional<Applications> optionalApplications = applicationsRepository.findById(applicationId);
        if (optionalApplications.isEmpty()) {
            throw new UserInfoException("Application not found with id: " + applicationId);
        }
        Applications applications = optionalApplications.get();
        applications.setIsAccepted(true);
        Applications savedApplication = applicationsRepository.save(applications);
        return ApplicationDto.applicationToApplicationDto(savedApplication);
    }

    @Override
    public List<ApplicationDto> getFreelancerApplicationsByApplicationStatus(Boolean isAccepted) {
        User currentFreelancer = authenticationService.getAuthenticatedUser();
        currentFreelancer.getRoles().forEach(role -> {
            if (role.getName().equals(Role.CLIENT)) {
                throw new UserInfoException("You are not authorized to view applications. Only freelancers can view applications");
            }
        });

        List<Applications> applications = applicationsRepository.findAllByFreelancerAndIsAccepted(currentFreelancer, isAccepted);
        return applications.stream().map(ApplicationDto::applicationToApplicationDto).toList();
    }
}
