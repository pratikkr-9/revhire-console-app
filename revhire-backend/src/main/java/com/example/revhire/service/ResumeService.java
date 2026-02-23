package com.example.revhire.service;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.revhire.dto.ResumeRequest;
import com.example.revhire.dto.ResumeResponse;
import com.example.revhire.entity.Resume;
import com.example.revhire.entity.User;
import com.example.revhire.repository.ResumeRepository;
import com.example.revhire.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeResponse saveResume(String email, ResumeRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElse(new Resume());

        resume.setObjective(request.getObjective());
        resume.setEducation(request.getEducation());
        resume.setExperience(request.getExperience());
        resume.setSkills(request.getSkills());
        resume.setProjects(request.getProjects());
        resume.setCertifications(request.getCertifications());
        resume.setUser(user);

        Resume saved = resumeRepository.save(resume);

        return mapToResponse(saved);
    }

    public ResumeResponse getResume(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return mapToResponse(resume);
    }

    public ResumeResponse uploadResumeFile(String email, MultipartFile file) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Resume file is required");
        }

        long maxSize = 2 * 1024 * 1024; // 2MB
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size exceeds 2MB limit");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                (!fileName.toLowerCase().endsWith(".pdf") &&
                 !fileName.toLowerCase().endsWith(".docx"))) {
            throw new RuntimeException("Only PDF or DOCX files allowed");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        String uploadDir = "uploads/resumes/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destination = new File(uploadDir + uniqueFileName);

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Create resume first"));

        resume.setUploadedFilePath(destination.getAbsolutePath());

        Resume saved = resumeRepository.save(resume);

        return mapToResponse(saved);
    }

    private ResumeResponse mapToResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .objective(resume.getObjective())
                .education(resume.getEducation())
                .experience(resume.getExperience())
                .skills(resume.getSkills())
                .projects(resume.getProjects())
                .certifications(resume.getCertifications())
                .uploadedFilePath(resume.getUploadedFilePath())
                .build();
    }
}