package com.example.revhire.service;

import com.example.revhire.dto.LoginRequest;
import com.example.revhire.dto.RegisterRequest;
import com.example.revhire.entity.User;
import com.example.revhire.enums.Role;
import com.example.revhire.repository.UserRepository;
import com.example.revhire.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User.UserBuilder userBuilder = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .location(request.getLocation())
                .role(request.getRole());

        if (request.getRole() == Role.JOB_SEEKER) {

            if (request.getEmploymentStatus() == null) {
                throw new RuntimeException("Employment status is required for job seekers");
            }

            userBuilder.employmentStatus(request.getEmploymentStatus());
        }


        User user = userBuilder.build();

        userRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}