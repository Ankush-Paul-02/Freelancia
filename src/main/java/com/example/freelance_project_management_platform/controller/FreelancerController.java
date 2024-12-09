package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.ApplicationRequestDto;
import com.example.freelance_project_management_platform.business.dto.DefaultResponseDto;
import com.example.freelance_project_management_platform.business.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.freelance_project_management_platform.business.dto.DefaultResponseDto.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/freelancer")
public class FreelancerController {

    private final ApplicationService applicationService;

    //! http://localhost:8081/api/v1/freelancer/submit-application
    @PostMapping("/submit-application")
    public ResponseEntity<DefaultResponseDto> submitApplication(@RequestBody ApplicationRequestDto applicationRequestDto) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "application",
                                applicationService.submitApplication(applicationRequestDto)
                        ),
                        "Application submitted successfully"
                )
        );
    }

    //! http://localhost:8081/api/v1/freelancer/applications/{isAccepted}
    @GetMapping("/applications/{isAccepted}")
    public ResponseEntity<DefaultResponseDto> getApplicationsByApplicationStatus(@PathVariable Boolean isAccepted) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(
                                "applications",
                                applicationService.getFreelancerApplicationsByApplicationStatus(isAccepted)
                        ),
                        "Applications retrieved successfully"
                )
        );
    }
}
