package io.dev.socialmediaplatform.usermanagement.user.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.exception.UserNotFoundException;
import io.dev.socialmediaplatform.usermanagement.user.dto.UserProfile;
import io.dev.socialmediaplatform.usermanagement.user.entity.User;
import io.dev.socialmediaplatform.usermanagement.user.repo.UserRepository;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> retrieveUserByEmail(String email) {
        log.info("Entering retrieveUserByEmail()");
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("Leaving retrieveUserByEmail()");
        return optionalUser;
    }

    @Override
    public User saveUser(User user) {
        log.info("Entering saveUser()");
        User savedUser = userRepository.save(user);
        log.info("Leaving saveUser()");
        return savedUser;
    }

    @Override
    public UserProfile retrieveUser(UUID userId) {
        log.info("Entering retrieveUser() with userId: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            log.error("User with id {} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(user.getEmail());
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        log.info("Leaving retrieveUser() with found user");
        return userProfile;
    }
}
