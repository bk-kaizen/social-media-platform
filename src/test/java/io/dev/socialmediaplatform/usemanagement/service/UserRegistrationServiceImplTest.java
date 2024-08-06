package io.dev.socialmediaplatform.usemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.dev.socialmediaplatform.exception.UserException;
import io.dev.socialmediaplatform.usermanagement.config.JwtService;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationRequest;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationResponse;
import io.dev.socialmediaplatform.usermanagement.user.entity.User;
import io.dev.socialmediaplatform.usermanagement.user.enumeration.Role;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;
import io.dev.socialmediaplatform.usermanagement.user.service.impl.UserRegistrationServiceImpl;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator fieldValidator;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    @DisplayName("Register User with Valid Request Provides Success Response")
    void registerUser_Success() {
        RegistrationRequest request = new RegistrationRequest("john", "doe", "user@example.com", "password");
        User user = User.builder().id(UUID.randomUUID()).firstName("john").lastName("doe").email("user@example.com")
                .password("encodedPassword").role(Role.USER).build();
        when(fieldValidator.validate(request)).thenReturn(Set.of());
        when(userService.retrieveUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(request.getEmail())).thenReturn("jwtToken");

        RegistrationResponse response = userRegistrationService.registerUser(request);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    @DisplayName("Register User with Invalid Request Throws Exception")
    void registerUser_UserAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest("firstName", "lastName", "user@example.com", "password");
        User existingUser = User.builder().id(UUID.randomUUID()).firstName("firstName").lastName("lastName")
                .email("user@example.com").password("encodedPassword").role(Role.USER).build();
        when(fieldValidator.validate(request)).thenReturn(Set.of());
        when(userService.retrieveUserByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        UserException exception = assertThrows(UserException.class, () -> {
            userRegistrationService.registerUser(request);
        });
        assertEquals("user email already exists!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

}
