package com.example.revhire.dto;


import lombok.Data;

@Data
public class UpdateJobSeekerProfileRequest {

    private Integer experienceYears;
    private String skills;
    private String education;
}
