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

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/accounts")
@Slf4j
public class AccountResource {
    private final AppUserService accountService;

    @GetMapping({"/admin/users", "/root/users"})
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok(accountService.getUsers());
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/admin/users/create", "/root/users/create"})
    public ResponseEntity<?> createAccount(@RequestBody AppUser account) {
        AppUser newAccount;
        try {
            newAccount = accountService.createAppUser(account);
        } catch (ResponseException e) {
            return e.response();
        }
        return ResponseEntity.ok(newAccount);
    }


    @GetMapping({"/roles"})
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(accountService.getRoles());
    }

    @PostMapping({"/root/users/addrole", "/admin/users/addrole"})
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        AppUser user;
        try {
            user = accountService.addRoleToUser(form.getUsername(), form.getRole());
        } catch (ResponseException e) {
            return e.response();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping({"/root/roles/add", "/admin/roles/add"})
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        try {
            return ResponseEntity.ok(accountService.saveRole(role));
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/admin/roles/retrieve", "/root/roles/retrieve"})
    public ResponseEntity<?> deleteRoleFromUser(@RequestBody RoleToUserForm form) {
        try {
            return ResponseEntity.ok(accountService.deleteRoleFromUser(form.getUsername(), form.getRole()));
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/root/roles/delete", "/admin/roles/delete"})
    public ResponseEntity<?> deleteRole(@RequestBody RoleNameForm roleName) {
        try {
            accountService.deleteRole(roleName.getRolename());
            return ResponseEntity.ok("Role " + roleName + " was deleted");
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/root/users/delete", "/admin/users/delete"})
    public ResponseEntity<?> deleteUser(@RequestBody UserNameForm userName) {
        try {
            accountService.deleteUser(userName.getUsername());
            return ResponseEntity.ok("Role " + userName + " was deleted");
        } catch (ResponseException e) {
            return e.response();
        }
    }

    @PostMapping({"/root/users/changepassword", "/admin/users/changepassword"})
    public ResponseEntity<?> changePassword(@RequestBody UserForm user) {
        try {
            return ResponseEntity.ok(accountService.changePassword(user.getUsername(), user.getPassword()));
        } catch (ResponseException e) {
            return e.response();
        }
    }


}

@Data
@ToString
class RoleNameForm {
    private String rolename;
}

@Data
@ToString
class UserNameForm {
    private String username;
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