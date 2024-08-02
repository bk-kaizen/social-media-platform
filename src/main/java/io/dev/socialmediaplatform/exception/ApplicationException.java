package io.dev.socialmediaplatform.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationException extends RuntimeException {

    private String message;
    private HttpStatus status;
    private List<String> errorMessages;

    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public ApplicationException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ApplicationException(List<String> errorMessages, HttpStatus status) {
        super();
        this.errorMessages = errorMessages;
        this.status = status;
    }

}
