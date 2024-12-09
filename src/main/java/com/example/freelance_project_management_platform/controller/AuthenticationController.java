package com.example.freelance_project_management_platform.controller;

import com.example.freelance_project_management_platform.business.dto.*;
import com.example.freelance_project_management_platform.business.service.AuthenticationService;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.freelance_project_management_platform.business.dto.DefaultResponseDto.Status.SUCCESS;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    //! POST http://localhost:8081/api/v1/auth/register
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto> register(
            @Valid @RequestBody RegisterUserDto registerUserDto
    ) {
        User registeredUser = authenticationService.register(registerUserDto);
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("user", registeredUser),
                        "User registered successfully.\nWe have sent a verification code to your email.\nPlease verify your account."
                )
        );
    }

    //! POST http://localhost:8081/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto> authenticate(
            @Valid @RequestBody LoginUserDto loginUserDto
    ) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .token(token)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("loginResponse", loginResponse),
                        "User authenticated successfully"
                )
        );
    }

    //! POST http://localhost:8081/api/v1/auth/verify
    @PostMapping("/verify")
    public ResponseEntity<DefaultResponseDto> verifyAccount(
            @Valid @RequestBody VerifyUserDto verifyUserDto
    ) {
        authenticationService.verifyUser(verifyUserDto);
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(),
                        "User verified successfully"
                )
        );
    }

    //! POST http://localhost:8081/api/v1/auth/resend-verification-code
    @PostMapping("/resend-verification-code")
    public ResponseEntity<DefaultResponseDto> resendVerificationCode(
            @Valid @RequestParam String email
    ) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of(),
                        "Verification code resent successfully"
                )
        );
    }
}
