package io.dev.socialmediaplatform.usemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import io.dev.socialmediaplatform.exception.UserException;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.service.impl.AuthenticationServiceImpl;
import io.dev.socialmediaplatform.usermanagement.config.JwtService;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Validator validator;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Given Valid Credentials Provides Success Response")
    void authenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(anyString())).thenReturn("jwtToken");
        when(validator.validate(request)).thenReturn(Set.of());

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
    }

    @Test
    @DisplayName("Given Invalid Credentials Throws Error Message")
    void authenticate_InvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new UserException("user not found!"));

        Exception exception = assertThrows(UserException.class, () -> {
            authenticationService.authenticate(request);
        });
        assertEquals("user not found!", exception.getMessage());
    }

}
