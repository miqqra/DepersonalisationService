package ru.nsu.backend.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts")
public class AccountResource {
    private final AppUserService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AppUser account) {
        AppUser newAccount = null;
        try {
            newAccount = accountService.createAppUser(account);
        } catch (ResponseException e) {
            return e.response();
        }
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAccount() {
        return ResponseEntity.ok(accountService.getUsers());
    }
}