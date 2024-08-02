package io.dev.socialmediaplatform.usermanagement.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) class representing the registration request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    /**
     * The first name of the user.
     */
    @NotNull(message ="First name must not be null")
    @NotBlank(message = "First name must not be empty")
    private String firstname;

    /**
     * The last name of the user.
     */
    private String lastname;

    /**
     * The email of the user.
     */
    @NotNull(message ="Email must not be null")
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Provide valid email")
    private String email;

    /**
     * The password of the user.
     */
    @NotNull(message ="Password must not be null")
    @NotBlank(message = "Password must not be empty")
    private String password;

}
