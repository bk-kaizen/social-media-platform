package io.dev.socialmediaplatform.usermanagement.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the user data with an ID and authentication token.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RegistrationResponse {
    /**
     * The unique identifier for the user.
     */
    private UUID userId;
    /**
     * The authentication token associated with the user.
     */
    private String token;
}