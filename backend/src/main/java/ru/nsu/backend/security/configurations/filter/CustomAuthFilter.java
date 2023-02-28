package ru.nsu.backend.security.configurations.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUserService;
import ru.nsu.backend.security.configurations.CustomSecurityConfig;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final AppUserService userService;

    public CustomAuthFilter(AuthenticationManager authenticationManager, AppUserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    private final String secretWord = CustomSecurityConfig.secretWord;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        log.info("User {} start authentication", username);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, password);


        try {
            Authentication authentication = authenticationManager.authenticate(token);
            return authentication;
        } catch (AuthenticationException exception) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(403);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ResponseException.Response(HttpStatus.FORBIDDEN, "Authentication error"));
            } catch (IOException ignored) {
                response.setStatus(500);
            }
            return null;
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secretWord.getBytes());
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        try {
            userService.updateAccessToken(user.getUsername(), token);
            userService.updateRefreshToken(user.getUsername(), refresh_token);
        } catch (ResponseException ignored) {
            response.setStatus(500);
        }
        response.setHeader("access_token", token);
        response.setHeader("refresh_token", refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info("Success with token {}!", user.getUsername());
        new ObjectMapper().writeValue(response.getOutputStream(),
                Map.of("access_token", token,
                        "refresh_token", refresh_token,
                        "roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()),
                        "username", user.getUsername()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.warn("Problem with authentication {} {}", request.getRequestURL(), failed.getMessage());
        response.setStatus(403);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),
                    Map.of("message", "problems with Authentication",
                            "status", response.getStatus()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
