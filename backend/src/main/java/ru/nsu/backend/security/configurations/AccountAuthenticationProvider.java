package ru.nsu.backend.security.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null || userDetails.getPassword() == null) {
            log.warn("Credentials is null");
            throw new BadCredentialsException("Credentials may not be null");
        }
        if (!encoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            log.warn("User {} hasn't credentials", userDetails.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
        log.info("User checking {} {}", userDetails.getUsername(), authentication.getCredentials());
    }


    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }
}