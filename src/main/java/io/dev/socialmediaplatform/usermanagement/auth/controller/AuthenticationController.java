package io.dev.socialmediaplatform.usermanagement.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationRequest;
import io.dev.socialmediaplatform.usermanagement.auth.dto.AuthenticationResponse;
import io.dev.socialmediaplatform.usermanagement.auth.service.api.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller class for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user authentication.
     * @param  authenticationRequest the authentication request containing user
     *                               credentials
     * @return                       the authentication response
     */
    @Operation(summary = "Authenticate a user",
            description = "Authenticates a user and returns an authentication token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = AuthenticationRequest.class))))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "404", description = "BadRequest",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 401,
                            "message": "UNAUTHORIZED",
                            "details": [
                                "Authentication is required to access this resource."
                            ]
                        }
                        """))) })
    @PostMapping("")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Entering authenticate()");
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        log.info("Leaving authenticate()");
        return authenticationResponse;
    }

}
