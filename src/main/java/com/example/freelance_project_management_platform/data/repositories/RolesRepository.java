package com.example.freelance_project_management_platform.data.repositories;

import com.example.freelance_project_management_platform.data.enums.Role;
import com.example.freelance_project_management_platform.data.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(Role name);
}
