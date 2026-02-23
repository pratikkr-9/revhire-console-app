package com.example.revhire.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // Public
            .requestMatchers("/api/auth/**").permitAll()

            // Swagger
            .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
            ).permitAll()

            // Employer-only
            .requestMatchers("/api/dashboard/employer").hasRole("EMPLOYER")
            .requestMatchers("/api/users/company-profile").hasRole("EMPLOYER")
            .requestMatchers("/api/applications/employer/**").hasRole("EMPLOYER")
            .requestMatchers("/api/applications/job/**").hasRole("EMPLOYER")
            .requestMatchers("/api/applications/*/status").hasRole("EMPLOYER")
            .requestMatchers("/api/applications/bulk-status").hasRole("EMPLOYER")

            // Job Seeker-only
            .requestMatchers("/api/applications/me").hasRole("JOB_SEEKER")
            .requestMatchers("/api/applications/*/withdraw").hasRole("JOB_SEEKER")
            .requestMatchers("/api/users/jobseeker-profile").hasRole("JOB_SEEKER")
            .requestMatchers("/api/resume/**").hasRole("JOB_SEEKER")
            .requestMatchers("/api/saved-jobs/**").hasRole("JOB_SEEKER")

            // All other endpoints require authentication
            .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}