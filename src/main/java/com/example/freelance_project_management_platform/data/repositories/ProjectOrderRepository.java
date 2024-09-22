package com.example.freelance_project_management_platform.data.repositories;

import com.example.freelance_project_management_platform.data.models.ProjectOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectOrderRepository extends JpaRepository<ProjectOrder, Long> {

    Optional<ProjectOrder> findByRazorpayOrderId(String razorpayOrderId);
}
