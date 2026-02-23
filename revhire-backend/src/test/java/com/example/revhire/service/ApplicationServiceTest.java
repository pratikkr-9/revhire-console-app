package com.example.revhire.service;


import com.example.revhire.entity.Application;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Mock
    private Authentication authentication;

    @Test
    void withdrawApplication_ShouldUpdateStatus() {

        Application app = new Application();
        app.setId(1L);
        app.setStatus(ApplicationStatus.APPLIED);

        when(authentication.getName()).thenReturn("user@test.com");

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(app));

        applicationService.withdrawApplication(1L, "user@test.com", authentication);

        assertEquals(ApplicationStatus.WITHDRAWN, app.getStatus());

        verify(applicationRepository).save(app);
    }
}