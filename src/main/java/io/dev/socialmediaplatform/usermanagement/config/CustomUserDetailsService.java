package io.dev.socialmediaplatform.usermanagement.config;

import io.dev.socialmediaplatform.usermanagement.user.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import io.dev.socialmediaplatform.usermanagement.user.dto.CustomUserDetails;

/**
 * CustomUserDetailsService class implements the UserDetailsService interface
 * and is responsible for loading user details from the UserService.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    /**
     * Loads user details for the given username from the UserService.
     * @param  username                  the username of the user to load details
     *                                   for.
     * @return                           UserDetails object representing the user
     *                                   details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.retrieveUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found." + username));
        return new CustomUserDetails(user);
    }
}
