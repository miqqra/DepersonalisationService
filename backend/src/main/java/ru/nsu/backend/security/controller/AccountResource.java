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

    @GetMapping({"/admin/users/info", "/root/users/info"})
    public ResponseEntity<?> getUserInfo(@RequestBody UserNameForm form) {
        try {
            return ResponseEntity.ok(accountService.getUser(form.getUsername()));
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

    @GetMapping({"/clear/cache"})
    public ResponseEntity<?> clearCache() {
        accountService.clearCache();
        return ResponseEntity.ok("Cache was cleared");
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
            accountService.deleteRole(roleName.getRole());
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

    @GetMapping("/token/info")
    public void getInfo(HttpServletRequest request, HttpServletResponse response) {
        log.info("User trying to refresh token");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String access_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(CustomSecurityConfig.secretWord.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(access_token);
                String username = decodedJWT.getSubject();
                AppUser user = accountService.getUser(username);
                String access_token1 = user.getAccess_token();
                if (!access_token1.equals(access_token)) {
                    ResponseException.throwResponse(HttpStatus.UNAUTHORIZED, "It's not current refresh token");
                }

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("username", user.getUsername(),
                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList())));
            } catch (ResponseException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseException.Response(e.httpStatus, e.reason));
                } catch (IOException ex) {
                    response.setStatus(500);
                }
                //return ResponseEntity.badRequest().body("There is no user");
            } catch (Exception e) {
                log.error("Error logging with {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            error);
                } catch (IOException ex) {
                    response.setStatus(500);
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "NOT TOKEN AUTHENTICATION");
            error.put("status", response.getStatus() + "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        error);
            } catch (IOException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }

        }
    }

    @GetMapping("token/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("User trying to logout");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(CustomSecurityConfig.secretWord.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = accountService.getUser(username);
                accountService.updateAccessToken(username, "");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("Information", "logout",
                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                                "username", user.getUsername()));
            } catch (ResponseException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {

                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseException.Response(e.httpStatus, e.reason));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                log.error("Error with logout {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
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
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "NOT TOKEN AUTHENTICATION");
            error.put("status", response.getStatus() + "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        error);
            } catch (IOException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        }
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("User trying to refresh token");
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
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                String newRefresh_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000))
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
                                "refresh_token", newRefresh_token,
                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                                "username", user.getUsername()));
            } catch (ResponseException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseException.Response(e.httpStatus, e.reason));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //return ResponseEntity.badRequest().body("There is no user");
            } catch (Exception e) {
                log.error("Error logging with {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
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
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "NOT TOKEN AUTHENTICATION");
            error.put("status", response.getStatus() + "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        error);
            } catch (IOException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        }
    }


}

@Data
@ToString
class RoleNameForm {
    private String role;
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