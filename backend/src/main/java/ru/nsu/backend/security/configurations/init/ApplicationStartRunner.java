package ru.nsu.backend.security.configurations.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserRepository;
import ru.nsu.backend.security.appUser.AppUserService;
import ru.nsu.backend.security.role.Role;
import ru.nsu.backend.security.role.RoleRepository;
import ru.nsu.backend.security.role.Roles;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationStartRunner implements CommandLineRunner {
    private final AppUserService userService;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role(Roles.USER));
        roleRepository.save(new Role(Roles.ADMIN));
        roleRepository.save(new Role(Roles.ROOT));
        generatingRoot();
        generatingAdmin();
        generatingUser();
    }


    void generatingRoot() {
        AppUser root = new AppUser();
        root.setUsername("root");
        root.setPassword("root");
        root.setId(1L);
        root.setPassword(encoder.encode(root.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Roles.USER).get());
        roles.add(roleRepository.findByName(Roles.ADMIN).get());
        roles.add(roleRepository.findByName(Roles.ROOT).get());
        root.setRoles(roles);
        appUserRepository.save(root);
    }

    void generatingAdmin() {
        AppUser root = new AppUser();
        root.setUsername("admin");
        root.setPassword("admin");
        root.setId(2L);
        root.setPassword(encoder.encode(root.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Roles.USER).get());
        roles.add(roleRepository.findByName(Roles.ADMIN).get());
        root.setRoles(roles);
        appUserRepository.save(root);
    }

    void generatingUser() {
        AppUser root = new AppUser();
        root.setUsername("user");
        root.setPassword("user");
        root.setId(3L);
        root.setPassword(encoder.encode(root.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Roles.USER).get());
        root.setRoles(roles);
        appUserRepository.save(root);
    }
}