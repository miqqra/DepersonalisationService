package ru.nsu.backend.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserService;
import ru.nsu.backend.security.configurations.CustomSecurityConfig;
import ru.nsu.backend.security.role.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequestMapping(path = "/accounts")
@AllArgsConstructor
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

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(CustomSecurityConfig.secretWord.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = accountService.getUser(username);
                String oldRefreshToken = user.getRefresh_token();
                if (!oldRefreshToken.equals(refresh_token)) {
                    ResponseException.throwResponse(HttpStatus.UNAUTHORIZED, "It's not current refresh token");
                }
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                String newRefresh_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                accountService.updateAccessToken(username, access_token);
                accountService.updateRefreshToken(username, newRefresh_token);
                response.setHeader("access_token", access_token);
                response.setHeader("refresh_token", newRefresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("access_token", access_token,
                                "refresh_token", newRefresh_token));
            } catch (ResponseException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseException.Response(e.httpStatus, e.reason));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //return ResponseEntity.badRequest().body("There is no user");
            } catch (Exception e) {
                log.error("Error logging in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                //response.sendError(HttpServletResponse.SC_FORBIDDEN);

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            error);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION");
            throw new RuntimeException("Refresh token is missing");
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