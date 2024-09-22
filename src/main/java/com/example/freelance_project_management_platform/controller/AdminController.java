package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.DefaultResponseDto;
import com.example.freelance_project_management_platform.business.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.freelance_project_management_platform.business.dto.DefaultResponseDto.Status.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    //! GET http://localhost:8081/api/v1/user/all
    @GetMapping("/users/all")
    public ResponseEntity<DefaultResponseDto> getAllUsers() {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("users", userService.getAllUsers()),
                        "All users fetched successfully"
                )
        );
    }
}
