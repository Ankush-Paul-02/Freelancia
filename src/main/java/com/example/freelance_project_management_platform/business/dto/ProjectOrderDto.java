package com.example.freelance_project_management_platform.business.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOrderDto {

    private ProjectResponseDto project;
    private UserDto client;

    private Double amount;
    private String status;
    private String razorpayOrderId;
    private Long createdAt;
    private Long updatedAt;
}
