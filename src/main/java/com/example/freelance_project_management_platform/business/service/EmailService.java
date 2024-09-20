package com.example.freelance_project_management_platform.business.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
