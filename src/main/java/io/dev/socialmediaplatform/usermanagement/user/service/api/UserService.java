package io.dev.socialmediaplatform.usermanagement.user.service.api;

import java.util.Optional;

import io.dev.socialmediaplatform.usermanagement.user.model.User;


public interface UserService {

    Optional<User> retrieveUserByEmail(String email);

    User saveUser(User user);
}
