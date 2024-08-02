package io.dev.socialmediaplatform.usermanagement.user.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationRequest;
import io.dev.socialmediaplatform.usermanagement.user.dto.RegistrationResponse;
import io.dev.socialmediaplatform.usermanagement.user.dto.UserProfile;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserRegistrationService;
import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    /**
     * Endpoint for user registration.
     * @param  registrationRequest the registration registrationRequest containing
     *                             user details
     * @return                     the registration response
     */
    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4",
                            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIiLCJpYXQiOjE3MjI1Nzg1MzQsImV4cCI6MTcyMjY2NDkzNH0.mlsX4znFZXdfHyy1stnYuyL5A_di_Dc8rzIKRvf6a7E"
                        }
                        """))),
        @ApiResponse(responseCode = "400", description = "Invalid registration details",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 400,
                            "message": "BAD_REQUEST",
                            "details": [
                                "Invalid registration details provided"
                            ]
                        }
                        """))),
        @ApiResponse(responseCode = "500", description = "Server error",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 500,
                            "message": "INTERNAL_SERVER_ERROR",
                            "details": [
                                "An unexpected error occurred"
                            ]
                        }
                        """))) })
    @PostMapping()
    public RegistrationResponse registerUser(@RequestBody RegistrationRequest registrationRequest)
            throws Exception {
        log.info("Entering registerUser()");
        RegistrationResponse registrationResponse = userRegistrationService.registerUser(registrationRequest);
        log.info("Leaving registerUser() #registrationResponse {}", registrationResponse);
        return registrationResponse;
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @Parameter(name = "userId", description = "ID of the user to retrieve",
            example = "d4c1c80e-5e1c-4c3d-933d-7fa4a4f9e9d2", required = true, in = ParameterIn.PATH)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "id": "d4c1c80e-5e1c-4c3d-933d-7fa4a4f9e9d2",
                            "firstName": "John"
                            "lastName": "Doe"
                        }
                        """))),
        @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 404,
                            "message":  "User not found for the given ID"
                        }
                        """))),
        @ApiResponse(responseCode = "400", description = "Invalid ID format",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 400,
                            "message": "BAD_REQUEST",
                            "details": [
                                "Invalid ID format"
                            ]
                        }
                        """))),
        @ApiResponse(responseCode = "401", description = "Unauthorized request",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 401,
                            "message": "UNAUTHORIZED",
                            "details": [
                                "Full authentication is required to access this resource"
                            ]
                        }
                        """))) })
    @GetMapping("/{userId}")
    public UserProfile retrieveUser(@PathVariable UUID userId) {
        log.info("Entering retrieveUser()");
        UserProfile userProfile = userService.retrieveUser(userId);
        log.info("Leaving retrieveUser()");
        return userProfile;
    }

}
