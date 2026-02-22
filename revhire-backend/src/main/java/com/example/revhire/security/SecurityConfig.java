package com.example.revhire.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()

                // Employer-only endpoints
                .requestMatchers("/api/jobs/create").hasRole("EMPLOYER")
                .requestMatchers("/api/jobs/update/**").hasRole("EMPLOYER")
                .requestMatchers("/api/jobs/delete/**").hasRole("EMPLOYER")
                .requestMatchers("/api/applications/view-applicants/**").hasRole("EMPLOYER")
                .requestMatchers("/api/applications/shortlist/**").hasRole("EMPLOYER")
                .requestMatchers("/api/applications/reject/**").hasRole("EMPLOYER")
                .requestMatchers("/api/dashboard/employer/**").hasRole("EMPLOYER")

                // Job seeker-only endpoints
                .requestMatchers("/api/applications/apply/**").hasRole("JOB_SEEKER")
                .requestMatchers("/api/applications/withdraw/**").hasRole("JOB_SEEKER")
                .requestMatchers("/api/resume/**").hasRole("JOB_SEEKER")
                .requestMatchers("/api/saved-jobs/**").hasRole("JOB_SEEKER")
                .requestMatchers("/api/dashboard/jobseeker/**").hasRole("JOB_SEEKER")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Stateless JWT
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

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}