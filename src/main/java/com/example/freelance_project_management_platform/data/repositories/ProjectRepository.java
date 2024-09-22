package com.example.freelance_project_management_platform.data.repositories;

import com.example.freelance_project_management_platform.data.models.Project;
import com.example.freelance_project_management_platform.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByClient(User user);
}
