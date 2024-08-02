package io.dev.socialmediaplatform.exception;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ApplicationException {
    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
