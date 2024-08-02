package io.dev.socialmediaplatform.usermanagement.user.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import io.dev.socialmediaplatform.usermanagement.user.enumeration.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a user entity in the system.
 */
@Entity
@Table(schema = "social_media", name = "\"user\"")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    /**
     * The unique identifier for the user.
     */
    @Id
    private UUID id;
    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The password for the user account.
     */
    private String password;
    /**
     * The first name of the user.
     */
    private String firstName;
    /**
     * The last name of the user.
     */
    private String lastName;
    /**
     * The role assigned to the user.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}
