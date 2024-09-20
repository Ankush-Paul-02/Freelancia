package com.example.freelance_project_management_platform.business.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String token;
    private Long expiresIn;
}
