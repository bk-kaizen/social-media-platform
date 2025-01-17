package io.dev.socialmediaplatform.usermanagement.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) class representing the authentication request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

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
