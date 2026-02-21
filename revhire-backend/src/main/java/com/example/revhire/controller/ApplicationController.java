package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.ApplyRequest;
import com.example.revhire.entity.Application;
import com.example.revhire.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public Application apply(@RequestBody ApplyRequest request,
                             Authentication authentication) {
        return applicationService.applyForJob(authentication.getName(), request);
    }

    @PostMapping("/{id}/withdraw")
    public Application withdraw(@PathVariable Long id,
                                @RequestParam String reason,
                                Authentication authentication) {
        return applicationService.withdrawApplication(authentication.getName(), id, reason);
    }

    @GetMapping("/me")
    public List<Application> myApplications(Authentication authentication) {
        return applicationService.viewMyApplications(authentication.getName());
    }
}
