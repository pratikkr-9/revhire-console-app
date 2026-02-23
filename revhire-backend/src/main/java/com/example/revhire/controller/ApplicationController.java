package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.ApplyRequest;
import com.example.revhire.dto.BulkStatusUpdateRequest;
import com.example.revhire.entity.Application;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.service.ApplicationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // APPLY FOR JOB
    @PostMapping("/{jobId}")
    public ResponseEntity<Application> apply(
            @PathVariable Long jobId,
            @RequestBody ApplyRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.applyForJob(jobId, request, authentication)
        );
    }

    // WITHDRAW APPLICATION
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            Authentication authentication) {

        applicationService.withdrawApplication(id, reason, authentication);
        return ResponseEntity.ok("Application withdrawn successfully");
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String note,
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.updateApplicationStatus(id, status, note, authentication)
        );
    }

    // VIEW MY APPLICATIONS (JOB SEEKER)
    @GetMapping("/me")
    public ResponseEntity<Page<Application>> myApplications(
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.searchApplicants(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        pageable
                )
        );
    }

    // VIEW APPLICANTS FOR A JOB (EMPLOYER)
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<Application>> viewApplicants(
            @PathVariable Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            Pageable pageable) {

        return ResponseEntity.ok(
                applicationService.searchApplicants(
                        jobId,
                        status,
                        null,
                        null,
                        null,
                        null,
                        pageable
                )
        );
    }

    // SEARCH / FILTER APPLICANTS
    @GetMapping("/employer/search")
    public ResponseEntity<Page<Application>> searchApplicants(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) LocalDateTime appliedAfter,
            @RequestParam(required = false) Integer experienceYears,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String education,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                applicationService.searchApplicants(
                        jobId,
                        status,
                        appliedAfter,
                        experienceYears,
                        skills,
                        education,
                        pageable
                )
        );
    }

    // BULK STATUS UPDATE (EMPLOYER)
    @PutMapping("/bulk-status")
    public ResponseEntity<String> bulkUpdate(
            @RequestBody BulkStatusUpdateRequest request,
            Authentication authentication) {

        applicationService.updateApplicationStatusBulk(
                request.getApplicationIds(),
                request.getStatus(),
                request.getEmployerNote(),
                authentication
        );

        return ResponseEntity.ok("Applications updated successfully");
    }
}