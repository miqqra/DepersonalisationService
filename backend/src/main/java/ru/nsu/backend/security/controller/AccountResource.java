package ru.nsu.backend.security.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserService;
import ru.nsu.backend.security.role.Role;
import ru.nsu.backend.security.role.Roles;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/accounts")
@Slf4j
public class AccountResource {
    private final AppUserService accountService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        System.out.println("WOWOWOWOWO");
        try {
            return ResponseEntity.ok(accountService.getUsers());
        } catch (ResponseException e) {
            return e.response();
        }
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

    @GetMapping("/admin")
    public ResponseEntity<?> getAccount() {
        try {
            return ResponseEntity.ok(accountService.getUsers());
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getRoles() {
        try {
            Roles.mustBeAdmin();
        } catch (ResponseException e) {
            e.response();
        }
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
        try {
            return ResponseEntity.ok(accountService.saveRole(role));
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping("/root/removerole")
    public ResponseEntity<?> deleteRoleFromUser(@RequestBody RoleToUserForm form) {
        try {
            return ResponseEntity.ok(accountService.deleteRoleFromUser(form.getUsername(), form.getRole()));
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/root/deleterole", "/admin/deleterole"})
    public ResponseEntity<?> deleteRole(@RequestBody RoleNameForm roleName) {
        try {
            accountService.deleteRole(roleName.getRoleName());
            return ResponseEntity.ok("Role " + roleName + " was deleted");
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/root/deleteuser", "/admin/deleteuser"})
    public ResponseEntity<?> deleteUser(@RequestBody UserNameForm userName) {
        try {
            accountService.deleteUser(userName.getUserName());
            return ResponseEntity.ok("Role " + userName + " was deleted");
        } catch (ResponseException e) {
            return e.response();
        }
    }


}

@Data
@ToString
class RoleNameForm {
    private String roleName;
}

@Data
@ToString
class UserNameForm {
    private String userName;
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