package io.dev.socialmediaplatform.usermanagement.auth.service.api;

import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.dto.RegisterRequest;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthenticationService {

    /**
     * Registers a new user.
     * @param  request the registration request containing user details
     * @return         the authentication response
     */
    AuthenticationResponse registerUser(RegisterRequest request) throws Exception;

    /**
     * Authenticates a user.
     * @param  request the authentication request containing user credentials
     * @return         the authentication response
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);


}
