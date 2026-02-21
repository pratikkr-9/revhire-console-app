package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.ApplyRequest;
import com.example.revhire.dto.BulkStatusUpdateRequest;
import com.example.revhire.entity.Application;
import com.example.revhire.enums.ApplicationStatus;
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
    
    @GetMapping("/job/{jobId}")
    public List<Application> viewApplicants(
            @PathVariable Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            Authentication authentication) {

        return applicationService.getApplicantsForJob(
                authentication.getName(),
                jobId,
                status);
    }
    @PutMapping("/{id}/status")
    public Application updateStatus(@PathVariable Long id,
                                    @RequestParam ApplicationStatus status,
                                    @RequestParam(required = false) String note,
                                    Authentication authentication) {

        return applicationService.updateApplicationStatus(
                authentication.getName(),
                id,
                status,
                note);
    }
    
    @PutMapping("/bulk-status")
    public List<Application> bulkUpdate(
            @RequestBody BulkStatusUpdateRequest request,
            Authentication authentication) {

        return applicationService.bulkUpdateStatus(
                authentication.getName(),
                request.getApplicationIds(),
                request.getStatus());
    }
}
