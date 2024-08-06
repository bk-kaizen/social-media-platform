package io.dev.socialmediaplatform.exception;

import org.springframework.http.HttpStatus;

public class UserException extends ApplicationException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, HttpStatus status) {
        super(message, status);
    }
}
