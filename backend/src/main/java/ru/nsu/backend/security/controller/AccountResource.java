package ru.nsu.backend.security.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserService;
import ru.nsu.backend.security.role.Role;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/accounts")
public class AccountResource {
    private final AppUserService accountService;

    @GetMapping("/root/users")
    public ResponseEntity<?> getUsers() {
//        System.out.println(accountService.getUsers());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok(accountService.getUsers());
    }

    @PostMapping("/root/adduser")
    public ResponseEntity<?> createAccount(@RequestBody AppUser account) {
        AppUser newAccount;
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

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(accountService.getRoles());
    }

    @PostMapping("/root/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        AppUser user;
        try {
            user = accountService.addRoleToUser(form.getUsername(), form.getRole());
        } catch (ResponseException e) {
            return e.response();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/root/addrole")
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        return ResponseEntity.ok(accountService.saveRole(role));
    }


}

@Data
class UserForm {
    private String username;
    private String password;
}

@Data
class RoleToUserForm {
    private String username;
    private String role;
}