package io.dev.socialmediaplatform.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(ex.getStatus().value());
        errorResponse.setMessage(ex.getStatus().name());
        details.add(ex.getMessage().split(":")[0]);
        errorResponse.setDetails(details);


        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostException ex) {
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(ex.getStatus().value());
        errorResponse.setMessage(ex.getStatus().name());
        details.add(ex.getMessage().split(":")[0]);
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleDynamoException(ApplicationException applicationException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(applicationException.getStatus().value());
        errorResponse.setMessage(applicationException.getStatus().name());
        String message = applicationException.getMessage();
        List<String> errorMessages = applicationException.getErrorMessages();
        if (Objects.isNull(message) && !errorMessages.isEmpty()) {
            errorResponse.setDetails(errorMessages);
        } else {
            errorResponse.setDetails(List.of(message));
        }

        return new ResponseEntity<>(errorResponse, applicationException.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(HttpStatus.valueOf(status.value()).name());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, status);
    }

}
