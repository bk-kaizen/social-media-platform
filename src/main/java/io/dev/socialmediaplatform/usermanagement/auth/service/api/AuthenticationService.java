package io.dev.socialmediaplatform.usermanagement.auth.service.api;

import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthenticationService {

       /**
     * Authenticates a user.
     * @param  request the authentication request containing user credentials
     * @return         the authentication response
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);


}
