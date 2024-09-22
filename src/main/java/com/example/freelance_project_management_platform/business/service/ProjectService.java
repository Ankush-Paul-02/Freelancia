package com.example.freelance_project_management_platform.business.service;

import com.example.freelance_project_management_platform.business.dto.CreateProjectRequestDto;
import com.example.freelance_project_management_platform.business.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(CreateProjectRequestDto createProjectRequestDto);

    List<ProjectResponseDto> getAllProjectByClient();
}
