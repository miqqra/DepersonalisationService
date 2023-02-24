package ru.nsu.backend.security.appUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.role.Role;
import ru.nsu.backend.security.role.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserService {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public AppUser saveUser(AppUser user) throws ResponseException {
        checkUser(user);
        return userRepository.save(user);
    }


    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public AppUser createAppUser(String userName, String password, List<Role> roles) throws ResponseException {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return createAppUser(userName, password, roles.stream().map(Role::getName).collect(Collectors.toSet()));
    }

    public AppUser createAppUser(String userName, String password, Set<String> roles) throws ResponseException {
        if (roles == null) {
            roles = new HashSet<>();
        }
        AppUser newUser = new AppUser();
        checkUser(newUser);
        List<String> notExists = new ArrayList<>();
        Set<Role> exists = new HashSet<>();
        for (String role : roles) {
            var foundedRole = roleRepository.findByName(role);
            foundedRole.ifPresent(exists::add);
            if (foundedRole.isEmpty()) {
                notExists.add(role);
            }
        }
        if (!notExists.isEmpty()) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "Roles not found: " + notExists);
        }
        newUser.setRoles(exists);
        newUser.setUsername(userName);
        newUser.setPassword(encoder.encode(password));
        return userRepository.save(newUser);
    }

    public AppUser createAppUser(String userName, String password) throws ResponseException {
        return createAppUser(userName, password, (Set<String>) null);
    }

    public AppUser createAppUser(AppUser appUser) throws ResponseException {
        checkUser(appUser);
        return createAppUser(appUser.getUsername(), appUser.getPassword(), (List<Role>) null);
    }

    public void addRoleToUser(String userName, String roleName) throws ResponseException {
        checkUserName(userName);
        var user = userRepository.findByUsername(userName);
        if (user.isEmpty()) {
            throw new ResponseException(HttpStatus.NOT_FOUND, "User not found: " + userName);
        }
        var role = roleRepository.findByName(userName);
        if (role.isEmpty()) {
            throw new ResponseException(HttpStatus.NOT_FOUND, "Role not found: " + roleName);
        }
        user.get().getRoles().add(role.get());
    }

    public void deleteRoleFromUser(String userName, String roleName) throws ResponseException {
        checkUserName(userName);
        var user = userRepository.findByUsername(userName);
        if (user.isEmpty()) {
            throw new ResponseException(HttpStatus.NOT_FOUND, "User not found: " + userName);
        }
        var role = roleRepository.findByName(userName);
        if (role.isEmpty()) {
            throw new ResponseException(HttpStatus.NOT_FOUND, "Role not found: " + roleName);
        }
        user.get().getRoles().remove(role.get());
    }

    public AppUser getUser(String username) throws ResponseException {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ResponseException(HttpStatus.NOT_FOUND, "User " + username + " not found");
        }
    }

    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }


    public static void checkUserName(String username) throws ResponseException {
        if (username == null || username.isBlank()) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "There is no username");
        }
        if (!username.matches("(\\w)+")) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "Bad username. Username must contains" +
                    "only digits letters");
        }
    }

    public static void checkPassword(String password) throws ResponseException {
        if (password == null || password.isBlank()) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "There is no username");
        }
        if (password.matches("(\\S)+")) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "Bad username. Username must contains" +
                    "only digits letters");
        }
    }

    public static void checkUser(AppUser user) throws ResponseException {
        checkUserName(user.getUsername());
        checkPassword(user.getPassword());
    }

    public boolean roleExists(Role role) {
        return roleExists(role.getName());
    }

    public boolean roleExists(String roleName) {
        return roleRepository.findByName(roleName).isPresent();
    }

}