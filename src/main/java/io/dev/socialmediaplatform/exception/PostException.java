package io.dev.socialmediaplatform.exception;

import org.springframework.http.HttpStatus;

public class PostException extends ApplicationException {
    public PostException(String message) {
        super(message);
    }

    public PostException(String message, HttpStatus status) {
        super(message, status);
    }
}
