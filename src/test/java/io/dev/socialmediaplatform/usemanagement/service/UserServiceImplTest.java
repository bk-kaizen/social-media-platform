package io.dev.socialmediaplatform.usemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import io.dev.socialmediaplatform.exception.UserException;
import io.dev.socialmediaplatform.usermanagement.user.dto.UserProfile;
import io.dev.socialmediaplatform.usermanagement.user.entity.User;
import io.dev.socialmediaplatform.usermanagement.user.repo.UserRepository;
import io.dev.socialmediaplatform.usermanagement.user.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Given Valid Email provides Success User Response")
    void retrieveUserByEmail_UserFound() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.retrieveUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("Given Unknown email provides empty optional")
    void retrieveUserByEmail_UserNotFound() {
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> result = userService.retrieveUserByEmail(email);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Given Valid User provides Success User Response")
    void saveUser_Success() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.saveUser(user);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Given Valid UserId Provides Success User profile Response")
    void retrieveUser_UserFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setFirstName("First");
        user.setLastName("Last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserProfile userProfile = userService.retrieveUser(userId);
        assertNotNull(userProfile);
        assertEquals("user@example.com", userProfile.getEmail());
        assertEquals("First", userProfile.getFirstName());
        assertEquals("Last", userProfile.getLastName());
    }

    @Test
    @DisplayName("Given Invalid userId Throws Exception")
    void retrieveUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            userService.retrieveUser(userId);
        });
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
