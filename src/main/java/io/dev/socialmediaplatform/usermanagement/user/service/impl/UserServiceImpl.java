package io.dev.socialmediaplatform.usermanagement.user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.usermanagement.user.model.User;
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
}
