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
}
