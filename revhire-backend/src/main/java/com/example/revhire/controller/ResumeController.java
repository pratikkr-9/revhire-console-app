package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.revhire.dto.ResumeRequest;
import com.example.revhire.dto.ResumeResponse;
import com.example.revhire.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResumeResponse saveResume(@RequestBody ResumeRequest request,
                                     Authentication authentication) {
        return resumeService.saveResume(authentication.getName(), request);
    }

    @GetMapping
    public ResumeResponse getResume(Authentication authentication) {
        return resumeService.getResume(authentication.getName());
    }

    @PostMapping("/upload")
    public ResumeResponse uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws Exception {

        return resumeService.uploadResumeFile(authentication.getName(), file);
    }
}