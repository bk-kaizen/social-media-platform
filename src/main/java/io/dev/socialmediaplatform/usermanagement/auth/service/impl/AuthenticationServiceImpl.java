package io.dev.socialmediaplatform.usermanagement.auth.service.impl;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.exception.UserNotFoundException;
import io.dev.socialmediaplatform.exception.ValidationExceptionUtils;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.service.api.AuthenticationService;
import io.dev.socialmediaplatform.usermanagement.config.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Validator validator;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Entering authenticate()");

        Set<ConstraintViolation<AuthenticationRequest>> constraintViolations =
                validator.validate(authenticationRequest);
        ValidationExceptionUtils.handleException(constraintViolations);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            var jwtToken = jwtService.generateToken(authenticationRequest.getEmail());
            log.info("Leaving authenticate()");
            return AuthenticationResponse.builder().accessToken(jwtToken).build();
        } else {
            throw new UserNotFoundException("user not found!", HttpStatus.NOT_FOUND);
        }

    }

}
