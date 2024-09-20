package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.service.UserService;
import com.example.freelance_project_management_platform.data.models.User;
import com.example.freelance_project_management_platform.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
