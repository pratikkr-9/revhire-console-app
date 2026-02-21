package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.EmployerDashboardResponse;
import com.example.revhire.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/employer")
    public EmployerDashboardResponse employerDashboard(
            Authentication authentication) {

        return dashboardService
                .getEmployerDashboard(authentication.getName());
    }
}