package com.example.revhire.dto;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.revhire.enums.JobType;

@Data
public class CreateJobRequest {

    private String title;
    private String description;

    private String requiredSkills;
    private Integer experienceYears;
    private String requiredEducation;

    private String location;

    private Double minSalary;
    private Double maxSalary;

    private JobType jobType;

    private LocalDateTime applicationDeadline;

    private Integer numberOfOpenings;
}