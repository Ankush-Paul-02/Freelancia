package com.example.freelance_project_management_platform.business.service;

import com.example.freelance_project_management_platform.business.dto.LoginUserDto;
import com.example.freelance_project_management_platform.business.dto.RegisterUserDto;
import com.example.freelance_project_management_platform.business.dto.VerifyUserDto;
import com.example.freelance_project_management_platform.data.models.User;

public interface AuthenticationService {

    User register(RegisterUserDto registerUserDto);

    User authenticate(LoginUserDto loginUserDto);

    void verifyUser(VerifyUserDto verifyUserDto);

    void resendVerificationCode(String email);

    User getAuthenticatedUser();
}
