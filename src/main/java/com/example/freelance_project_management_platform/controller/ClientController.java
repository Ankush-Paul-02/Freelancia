package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.CreateProjectRequestDto;
import com.example.freelance_project_management_platform.business.dto.DefaultResponseDto;
import com.example.freelance_project_management_platform.business.service.ApplicationService;
import com.example.freelance_project_management_platform.business.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.freelance_project_management_platform.business.dto.DefaultResponseDto.Status.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;

    //! http://localhost:8081/api/v1/client/project/create
    @PostMapping("/project/create")
    public ResponseEntity<DefaultResponseDto> createProject(@RequestBody CreateProjectRequestDto project) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("project", projectService.createProject(project)),
                        "Project created successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/client/projects
    @GetMapping("/projects")
    public ResponseEntity<DefaultResponseDto> getAllProjectsByClient() {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("projects", projectService.getAllProjectByClient()),
                        "Projects fetched successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/client/project/{projectId}/applicants
    @GetMapping("/project/{projectId}/applicants")
    public ResponseEntity<DefaultResponseDto> getApplicantsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("applicants", applicationService.getApplicationsByProjectId(projectId)),
                        "Applicants fetched successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/client/application/{applicationId}/accept
    @PutMapping("/application/{applicationId}/accept")
    public ResponseEntity<DefaultResponseDto> acceptApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("application", applicationService.acceptApplication(applicationId)),
                        "Application accepted successfully"
                )
        );
    }
}
