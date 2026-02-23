package com.example.revhire.entity;

import com.example.revhire.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    private String phone;

    private String location;

    private String employmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "education")
    private String education;
    
 // =============================
 // Employer Company Details
 // =============================

 @Column(name = "company_name")
 private String companyName;

 @Column(name = "industry")
 private String industry;

 @Column(name = "company_size")
 private String companySize;

 @Column(name = "company_description", columnDefinition = "TEXT")
 private String companyDescription;

 @Column(name = "company_website")
 private String companyWebsite;

 @Column(name = "company_location")
 private String companyLocation;
    
    
}