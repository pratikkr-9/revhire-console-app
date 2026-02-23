package com.example.revhire.controller;


import com.example.revhire.dto.UpdateCompanyProfileRequest;
import com.example.revhire.dto.UpdateJobSeekerProfileRequest;
import com.example.revhire.entity.User;
import com.example.revhire.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PutMapping("/company-profile")
    public ResponseEntity<User> updateCompanyProfile(
            @RequestBody UpdateCompanyProfileRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                authService.updateCompanyProfile(request, authentication)
        );
    }
    
    
    @PutMapping("/jobseeker-profile")
    public ResponseEntity<User> updateJobSeekerProfile(
            @RequestBody UpdateJobSeekerProfileRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                authService.updateJobSeekerProfile(request, authentication)
        );
    }
}