package com.example.revhire.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.ResumeRequest;
import com.example.revhire.entity.Resume;
import com.example.revhire.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public Resume saveResume(@RequestBody ResumeRequest request,
                             Authentication authentication) {
        return resumeService.saveResume(authentication.getName(), request);
    }

    @GetMapping
    public Resume getResume(Authentication authentication) {
        return resumeService.getResume(authentication.getName());
    }
}
