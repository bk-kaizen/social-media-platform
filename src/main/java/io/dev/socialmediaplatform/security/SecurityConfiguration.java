package io.dev.socialmediaplatform.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import io.dev.socialmediaplatform.exception.ErrorResponse;
import io.dev.socialmediaplatform.usermanagement.config.CustomUserDetailsService;
import io.dev.socialmediaplatform.usermanagement.config.JwtAuthenticationConfig;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SecurityConfiguration class is responsible for configuring the security
 * settings of the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    /**
     * Configures the security filter chain with the specified HTTP security
     * settings.
     * @param  http      the HttpSecurity object to configure.
     * @return           the SecurityFilterChain object representing the configured
     *                   security filter chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/actuator/health", "/api/api-docs/**", "/api/swagger-ui/**",
                                "/api/swagger/**", "/api/auth/**", "/api/users/**")
                        .permitAll().anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationConfig, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()));
        return http.build();

    }

    /**
     * Creates a PasswordEncoder bean for encoding and validating passwords.
     * @return the PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider to use the CustomUserDetailsService
     * and the PasswordEncoder.
     * @return the AuthenticationProvider object.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse errorResponse = buildErrorResponse(response, authException);
            buildServletOutputStream(response, errorResponse);
        };
    }

    private void buildServletOutputStream(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        ServletOutputStream out = response.getOutputStream();
        new ObjectMapper().writeValue(out, errorResponse);
        out.flush();
    }

    private ErrorResponse buildErrorResponse(HttpServletResponse httpServletResponse, Exception ex) {
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(httpServletResponse.getStatus());
        errorResponse.setMessage(HttpStatus.valueOf(httpServletResponse.getStatus()).name());
        details.add(ex.getLocalizedMessage().split(":")[0]);
        errorResponse.setDetails(details);
        return errorResponse;
    }
}
