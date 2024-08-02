package io.dev.socialmediaplatform.usermanagement.user.service.api;

import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationRequest;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationResponse;

public interface UserRegistrationService {

    /**
     * Registers a new user in the system.
     * @param  registrationRequest The registration request containing user details.
     * @return                     RegistrationResponse A response object containing
     *                             the result of the registration operation
     */
    RegistrationResponse registerUser(RegistrationRequest registrationRequest);
}
