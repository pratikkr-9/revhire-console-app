package com.example.revhire.dto;


import lombok.Data;

@Data
public class ResumeRequest {

    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    private String certifications;
}
