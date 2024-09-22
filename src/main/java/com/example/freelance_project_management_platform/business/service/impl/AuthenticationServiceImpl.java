package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.dto.LoginUserDto;
import com.example.freelance_project_management_platform.business.dto.RegisterUserDto;
import com.example.freelance_project_management_platform.business.dto.VerifyUserDto;
import com.example.freelance_project_management_platform.business.service.AuthenticationService;
import com.example.freelance_project_management_platform.business.service.EmailService;
import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import com.example.freelance_project_management_platform.data.enums.Role;
import com.example.freelance_project_management_platform.data.enums.UserStatus;
import com.example.freelance_project_management_platform.data.models.Roles;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.data.repositories.RolesRepository;
import com.example.freelance_project_management_platform.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RolesRepository rolesRepository;

    @Override
    public User register(RegisterUserDto registerUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(registerUserDto.getEmail());
        if (userOptional.isPresent()) {
            throw new UserInfoException("User with email " + registerUserDto.getEmail() + " already exists");
        }

        if (!Role.FREELANCER.name().equals(registerUserDto.getRole()) && !Role.CLIENT.name().equals(registerUserDto.getRole())) {
            throw new UserInfoException("Invalid role");
        }

        Optional<Roles> optionalRole = rolesRepository.findByName(Role.valueOf(registerUserDto.getRole()));
        if (optionalRole.isEmpty()) {
            throw new UserInfoException("Role does not exist");
        }
        Roles role = optionalRole.get();

        User user = User.builder()
                .name(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .roles(Set.of(role))
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .status(UserStatus.ACTIVE)
                .enabled(false)
                .verificationCode(generateVerificationCode())
                .verificationCodeExpirationTime(LocalDateTime.now().plusMinutes(15))
                .build();
        sendVerificationEmail(user);
        User save = userRepository.save(user);
        log.info("User password: {}", user.getPassword());
        return save;
    }

    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserInfoException("User with email " + loginUserDto.getEmail() + " does not exist");
        }
        User user = userOptional.get();
        if (!user.getEnabled()) {
            throw new UserInfoException("User is not verified");
        }
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            throw new UserInfoException("Invalid password");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserDto.getEmail(),
                            loginUserDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UserInfoException("Something went wrong: " + e.getMessage());
        }
        return user;
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(verifyUserDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserInfoException("User with email " + verifyUserDto.getEmail() + " does not exist");
        }
        User user = userOptional.get();
        if (user.getVerificationCodeExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserInfoException("Verification code has expired");
        }
        if (!user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {
            throw new UserInfoException("Invalid verification code");
        }
        user.setEnabled(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationCode(null);
        user.setVerificationCodeExpirationTime(null);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationCode(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserInfoException("User with email " + email + " does not exist");
        }
        User user = userOptional.get();
        if (user.isEnabled()) {
            throw new UserInfoException("User is already verified");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpirationTime(LocalDateTime.now().plusHours(1));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String message = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            throw new UserInfoException("Failed to send verification email");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
