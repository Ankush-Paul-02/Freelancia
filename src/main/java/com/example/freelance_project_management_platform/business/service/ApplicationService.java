package com.example.freelance_project_management_platform.business.service;

import com.example.freelance_project_management_platform.business.dto.ApplicationDto;
import com.example.freelance_project_management_platform.business.dto.ApplicationRequestDto;

import java.util.List;

public interface ApplicationService {

    ApplicationDto submitApplication(ApplicationRequestDto applicationRequestDto);

    List<ApplicationDto> getApplicationsByProjectId(Long projectId);

    ApplicationDto acceptApplication(Long applicationId);

    List<ApplicationDto> getFreelancerApplicationsByApplicationStatus(Boolean isAccepted);
}
