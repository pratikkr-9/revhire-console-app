package com.example.revhire.dto;


import lombok.Data;

@Data
public class UpdateCompanyProfileRequest {

    private String companyName;
    private String industry;
    private String companySize;
    private String companyDescription;
    private String companyWebsite;
    private String companyLocation;
}
