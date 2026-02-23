package com.example.revhire.service;

import com.example.revhire.dto.LoginRequest;
import com.example.revhire.dto.RegisterRequest;
import com.example.revhire.dto.UpdateCompanyProfileRequest;
import com.example.revhire.dto.UpdateJobSeekerProfileRequest;
import com.example.revhire.entity.User;
import com.example.revhire.enums.Role;
import com.example.revhire.repository.UserRepository;
import com.example.revhire.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // ===============================
    // REGISTER
    // ===============================
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        // Common fields
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());
        user.setRole(request.getRole());

        // ===============================
        // Employer Specific Handling
        // ===============================
        if (request.getRole() == Role.EMPLOYER) {

            if (request.getCompanyName() == null || request.getIndustry() == null) {
                throw new RuntimeException("Company name and industry are required for employer registration");
            }

            user.setCompanyName(request.getCompanyName());
            user.setIndustry(request.getIndustry());
            user.setCompanySize(request.getCompanySize());
            user.setCompanyDescription(request.getCompanyDescription());
            user.setCompanyWebsite(request.getCompanyWebsite());
            user.setCompanyLocation(request.getCompanyLocation());
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    // ===============================
    // LOGIN
    // ===============================
    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateToken(user);
    }
    
    
	public User updateCompanyProfile(UpdateCompanyProfileRequest request, Authentication authentication) {

		User employer = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (employer.getRole() != Role.EMPLOYER) {
			throw new RuntimeException("Only employers can update company profile");
		}

		employer.setCompanyName(request.getCompanyName());
		employer.setIndustry(request.getIndustry());
		employer.setCompanySize(request.getCompanySize());
		employer.setCompanyDescription(request.getCompanyDescription());
		employer.setCompanyWebsite(request.getCompanyWebsite());
		employer.setCompanyLocation(request.getCompanyLocation());

		return userRepository.save(employer);
	}
	
	public User updateJobSeekerProfile(UpdateJobSeekerProfileRequest request, Authentication authentication) {

		User user = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (user.getRole() != Role.JOB_SEEKER) {
			throw new RuntimeException("Only job seekers can update profile");
		}

		user.setExperienceYears(request.getExperienceYears());
		user.setSkills(request.getSkills());
		user.setEducation(request.getEducation());

		return userRepository.save(user);
	}
}