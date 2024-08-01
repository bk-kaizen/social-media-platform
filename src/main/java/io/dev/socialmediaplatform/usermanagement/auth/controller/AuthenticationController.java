package io.dev.socialmediaplatform.usermanagement.auth.controller;

import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.dto.RegisterRequest;
import io.dev.socialmediaplatform.usermanagement.auth.service.api.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user registration.
     * @param  registerRequest the registration registerRequest containing user
     *                         details
     * @return                 the authentication response
     */
    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        return authenticationService.registerUser(registerRequest);
    }

    /**
     * Endpoint for user authentication.
     * @param  request the authentication request containing user credentials
     * @return         the authentication response
     */
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

}
