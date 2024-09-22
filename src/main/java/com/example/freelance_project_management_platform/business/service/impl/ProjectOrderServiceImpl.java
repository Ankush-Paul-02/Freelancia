package com.example.freelance_project_management_platform.business.service.impl;

import com.example.freelance_project_management_platform.business.dto.ProjectOrderDto;
import com.example.freelance_project_management_platform.business.dto.ProjectResponseDto;
import com.example.freelance_project_management_platform.business.dto.UserDto;
import com.example.freelance_project_management_platform.business.service.ProjectOrderService;
import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import com.example.freelance_project_management_platform.data.enums.ProjectOrderStatus;
import com.example.freelance_project_management_platform.data.enums.ProjectStatus;
import com.example.freelance_project_management_platform.data.models.Project;
import com.example.freelance_project_management_platform.data.models.ProjectOrder;
import com.example.freelance_project_management_platform.data.repositories.ProjectOrderRepository;
import com.example.freelance_project_management_platform.data.repositories.ProjectRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectOrderServiceImpl implements ProjectOrderService {

    private final ProjectOrderRepository projectOrderRepository;
    private final ProjectRepository projectRepository;

    @Value("${application.razorpay.key}")
    private String razorpayKey;

    @Value("${application.razorpay.secret}")
    private String razorpaySecret;

    @Override
    public ProjectOrderDto createProjectOrder(Long projectId) throws RazorpayException {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new UserInfoException("Project not found with id " + projectId);
        }
        Project project = optionalProject.get();
        if (project.getProjectStatus() != ProjectStatus.COMPLETED) {
            throw new UserInfoException("Project is not completed yet");
        }

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", project.getBudget() * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt#" + project.getId());
        JSONObject notes = new JSONObject();
        notes.put("project_id", project.getId());
        notes.put("client_id", project.getClient().getId());
        notes.put("freelancer_id", project.getFreelancer().getId());
        orderRequest.put("notes", notes);

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        log.info("Razorpay order created: {}", razorpayOrder);

        ProjectOrder projectOrder = ProjectOrder.builder()
                .project(project)
                .client(project.getClient())
                .amount(project.getBudget())
                .razorpayOrderId(razorpayOrder.get("id"))
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .status(ProjectOrderStatus.valueOf(razorpayOrder.get("status").toString().toUpperCase()))
                .build();

        ProjectOrder savedProjectOrder = projectOrderRepository.save(projectOrder);
        return ProjectOrderDto.builder()
                .project(
                        ProjectResponseDto.projectToProjectResponseDto(savedProjectOrder.getProject())
                )
                .client(
                        UserDto.builder()
                                .name(savedProjectOrder.getClient().getName())
                                .email(savedProjectOrder.getClient().getEmail())
                                .build()
                )
                .amount(savedProjectOrder.getAmount())
                .razorpayOrderId(savedProjectOrder.getRazorpayOrderId())
                .status(savedProjectOrder.getStatus().name())
                .createdAt(savedProjectOrder.getCreatedAt())
                .updatedAt(savedProjectOrder.getUpdatedAt())
                .build();
    }

    @Override
    public ProjectOrderDto handlePaymentCallback(Map<String, String> data) throws RazorpayException {
        String razorpayOrderId = data.get("razorpay_order_id");
        Optional<ProjectOrder> optionalProjectOrder = projectOrderRepository.findByRazorpayOrderId(razorpayOrderId);
        if (optionalProjectOrder.isEmpty()) {
            throw new UserInfoException("Project order not found with razorpay order id " + razorpayOrderId);
        }
        ProjectOrder projectOrder = optionalProjectOrder.get();
        projectOrder.setStatus(ProjectOrderStatus.PAID);
        projectOrder.setUpdatedAt(System.currentTimeMillis());
        projectOrderRepository.save(projectOrder);

        return ProjectOrderDto.builder()
                .project(
                        ProjectResponseDto.projectToProjectResponseDto(projectOrder.getProject())
                )
                .client(
                        UserDto.builder()
                                .name(projectOrder.getClient().getName())
                                .email(projectOrder.getClient().getEmail())
                                .build()
                )
                .amount(projectOrder.getAmount())
                .razorpayOrderId(projectOrder.getRazorpayOrderId())
                .status(projectOrder.getStatus().name())
                .createdAt(projectOrder.getCreatedAt())
                .updatedAt(projectOrder.getUpdatedAt())
                .build();
    }
}
