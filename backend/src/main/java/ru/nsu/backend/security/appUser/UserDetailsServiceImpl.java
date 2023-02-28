package ru.nsu.backend.security.appUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.backend.exceptions.ResponseException;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser account;
        try {
            account = accountService.getUser(username);
        } catch (ResponseException e) {
            log.error("User {} doesn't exist", username);
            throw new UsernameNotFoundException("User doesn't exist");
        }
        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            log.error("User doesn't has role");
            throw new UsernameNotFoundException("User has no roles");
        }
        log.info("User {} try to connect with roles {}", username, account.getRoles());
        Collection<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(toList());
        return new User(account.getUsername(), account.getPassword(), account.isEnabled(),
                !account.isExpired(), !account.isCredentialsexpired(), !account.isLocked(), authorities);
    }
}