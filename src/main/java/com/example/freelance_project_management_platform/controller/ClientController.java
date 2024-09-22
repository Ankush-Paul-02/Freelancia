package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.CreateProjectRequestDto;
import com.example.freelance_project_management_platform.business.dto.DefaultResponseDto;
import com.example.freelance_project_management_platform.business.service.ApplicationService;
import com.example.freelance_project_management_platform.business.service.ProjectOrderService;
import com.example.freelance_project_management_platform.business.service.ProjectService;
import com.razorpay.RazorpayException;
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
    private final ProjectOrderService projectOrderService;

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

    //! http://localhost:8081/api/v1/client/project/{projectId}/status/{projectStatus}
    @PutMapping("/project/{projectId}/status/{projectStatus}")
    public ResponseEntity<DefaultResponseDto> updateProject(@PathVariable Long projectId, @PathVariable String projectStatus) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("project", projectService.updateProject(projectId, projectStatus)),
                        "Project updated successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/client/project/{projectId}/order
    @PostMapping("/project/{projectId}/order")
    public ResponseEntity<DefaultResponseDto> createProjectOrder(@PathVariable Long projectId) throws RazorpayException {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("projectOrder", projectOrderService.createProjectOrder(projectId)),
                        "Project order created successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/client/payment/callback
    @PostMapping("/payment/callback")
    public ResponseEntity<DefaultResponseDto> handlePaymentCallback(@RequestBody Map<String, String> payload) throws RazorpayException {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("payment", projectOrderService.handlePaymentCallback(payload)),
                        "Payment handled successfully"
                )
        );
    }
}
