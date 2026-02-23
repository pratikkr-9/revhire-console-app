package com.example.revhire.dto;



import com.example.revhire.enums.ApplicationStatus;
import lombok.Data;

import java.util.List;

@Data
public class BulkStatusUpdateRequest {

    private List<Long> applicationIds;

    private ApplicationStatus status;

    private String employerNote;  
}