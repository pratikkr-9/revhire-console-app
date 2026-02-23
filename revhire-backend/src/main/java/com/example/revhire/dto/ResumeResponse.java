package com.example.revhire.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeResponse {

    private Long id;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    private String certifications;
    private String uploadedFilePath;
}