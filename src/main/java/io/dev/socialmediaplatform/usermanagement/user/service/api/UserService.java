package io.dev.socialmediaplatform.usermanagement.user.service.api;

import java.util.Optional;
import java.util.UUID;

import io.dev.socialmediaplatform.exception.UserException;
import io.dev.socialmediaplatform.usermanagement.user.dto.UserProfile;
import io.dev.socialmediaplatform.usermanagement.user.entity.User;

public interface UserService {

    /**
     * Retrieves a user by their email address.
     * @param  email The email address of the user to retrieve. It must be a valid
     *               email format.
     * @return       An {@link Optional} containing the user if found, otherwise an
     *               empty {@link Optional}.
     */
    Optional<User> retrieveUserByEmail(String email);

    /**
     * Saves a new or existing user to the system.
     * @param  user The user object to be saved
     * @return      The saved {@link User} object. .
     */
    User saveUser(User user);

    /**
     * Retrieves a user profile by their unique identifier.
     * @param  userId                The unique identifier of the user whose profile
     *                               is to be retrieved. It must be a valid UUID.
     * @return                       The {@link UserProfile} object containing the
     *                               user's profile information.
     * @throws UserException If no user with the given ID exists.
     */
    UserProfile retrieveUser(UUID userId);
}
