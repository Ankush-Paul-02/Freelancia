package com.example.freelance_project_management_platform.business.service;

import com.example.freelance_project_management_platform.business.dto.ProjectOrderDto;
import com.razorpay.RazorpayException;

import java.util.Map;

public interface ProjectOrderService {

    ProjectOrderDto createProjectOrder(Long projectId) throws RazorpayException;

    ProjectOrderDto handlePaymentCallback(Map<String, String> data) throws RazorpayException;
}
