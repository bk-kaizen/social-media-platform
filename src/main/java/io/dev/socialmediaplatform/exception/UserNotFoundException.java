package io.dev.socialmediaplatform.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
