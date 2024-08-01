package io.dev.socialmediaplatform.usermanagement.auth.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.dto.RegisterRequest;
import io.dev.socialmediaplatform.usermanagement.auth.service.api.AuthenticationService;
import io.dev.socialmediaplatform.usermanagement.config.JwtService;
import io.dev.socialmediaplatform.usermanagement.user.enumeration.Role;
import io.dev.socialmediaplatform.usermanagement.user.model.User;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) throws Exception {
        Optional<User> optionalUser = userService.retrieveUserByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            throw new Exception("user already exists!");
        }

        var user = User.builder().firstName(request.getFirstname()).lastName(request.getLastname())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                .build();
        var savedUser = userService.saveUser(user);
        var jwtToken = jwtService.generateToken(savedUser.getEmail());
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authenticate.isAuthenticated()) {
            var jwtToken = jwtService.generateToken(request.getEmail());
            return AuthenticationResponse.builder().accessToken(jwtToken).build();
        } else {
            throw new UsernameNotFoundException("user not found!");
        }

    }

}
