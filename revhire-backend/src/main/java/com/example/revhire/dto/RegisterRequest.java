package com.example.revhire.dto;


import com.example.revhire.enums.Role;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String location;
    private String employmentStatus;
    private Role role;
    
 // Employer specific fields (optional)
    private String companyName;
    private String industry;
    private String companySize;
    private String companyDescription;
    private String companyWebsite;
    private String companyLocation;
}
