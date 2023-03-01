package ru.nsu.backend.security.configurations;

public class CustomSecurityConfig {

    public static final String secretWord = "aboba";
    public static final long accessTokenLifetime = 1000 * 60 * 15;
    public static final long refreshTokenLifetime = 1000 * 60 * 60 * 24;
}
