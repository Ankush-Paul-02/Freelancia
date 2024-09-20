package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.DefaultResponseDto;
import com.example.freelance_project_management_platform.business.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //! GET http://localhost:8081/api/v1/user/all
    @GetMapping("/all")
    public ResponseEntity<DefaultResponseDto> getAllUsers() {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        DefaultResponseDto.Status.SUCCESS,
                        Map.of("users", userService.getAllUsers()),
                        "All users fetched successfully"
                )
        );
    }
}
