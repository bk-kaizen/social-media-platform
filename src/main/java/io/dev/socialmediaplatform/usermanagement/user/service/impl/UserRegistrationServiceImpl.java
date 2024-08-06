package io.dev.socialmediaplatform.usermanagement.user.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.exception.UserException;
import io.dev.socialmediaplatform.exception.ValidationExceptionUtils;
import io.dev.socialmediaplatform.usermanagement.config.JwtService;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationRequest;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationResponse;
import io.dev.socialmediaplatform.usermanagement.user.entity.User;
import io.dev.socialmediaplatform.usermanagement.user.enumeration.Role;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserRegistrationService;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Validator fieldValidator;

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        log.info("Entering registerUser()");

        Set<ConstraintViolation<RegistrationRequest>> constraintViolations =
                fieldValidator.validate(registrationRequest);
        ValidationExceptionUtils.handleException(constraintViolations);
        Optional<User> optionalUser = userService.retrieveUserByEmail(registrationRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserException("user email already exists!", HttpStatus.NOT_FOUND);
        }

        var savedUser = buildAndSaveUser(registrationRequest);
        var jwtToken = jwtService.generateToken(savedUser.getEmail());
        RegistrationResponse registrationResponse =
                RegistrationResponse.builder().userId(savedUser.getId()).token(jwtToken).build();
        log.info("Leaving registerUser() #registrationResponse.userId {}", registrationResponse.getUserId());
        return registrationResponse;
    }

    private User buildAndSaveUser(RegistrationRequest registrationRequest) {
        log.info("Entering buildAndSaveUser()");
        var user = User.builder().id(UUID.randomUUID()).firstName(registrationRequest.getFirstname())
                .lastName(registrationRequest.getLastname()).email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword())).role(Role.USER).build();
        var savedUser = userService.saveUser(user);
        log.info("Leaving buildAndSaveUser()");
        return savedUser;
    }
}
