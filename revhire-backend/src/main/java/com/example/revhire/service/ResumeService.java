package com.example.revhire.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.ResumeRequest;
import com.example.revhire.entity.Resume;
import com.example.revhire.entity.User;
import com.example.revhire.repository.ResumeRepository;
import com.example.revhire.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public Resume saveResume(String email, ResumeRequest request) {

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

        return resumeRepository.save(resume);
    }

    public Resume getResume(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }
}
