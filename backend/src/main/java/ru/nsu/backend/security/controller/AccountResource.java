package ru.nsu.backend.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts")
public class AccountResource {
    private final AppUserService accountService;

    @PostMapping
    public ResponseEntity<AppUser> createAccount(@RequestBody AppUser account) {
        AppUser newAccount = accountService.createAppUser(account);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAccount() {
        return ResponseEntity.ok(accountService.getAccounts());
    }
}