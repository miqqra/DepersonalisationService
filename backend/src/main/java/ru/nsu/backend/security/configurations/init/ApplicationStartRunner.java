package ru.nsu.backend.security.configurations.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nsu.backend.security.appUser.AppUser;
import ru.nsu.backend.security.appUser.AppUserRepository;
import ru.nsu.backend.security.appUser.AppUserServiceImpl;
import ru.nsu.backend.security.role.Role;
import ru.nsu.backend.security.role.RoleRepository;
import ru.nsu.backend.security.role.RoleServiceImpl;
import ru.nsu.backend.security.role.StaticRoles;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class ApplicationStartRunner implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final RoleServiceImpl roleService;
    private final AppUserServiceImpl appUserService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        Role roleUser = new Role(1L, "123", StaticRoles.USER);
        Role roleAdmin = new Role(2L, "456", StaticRoles.ADMIN);
        Role roleSuperAdmin = new Role(3L, "789", StaticRoles.SUPER_ADMIN);
        roleRepository.saveAll(asList(roleUser, roleAdmin, roleSuperAdmin));

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
        roles.add(roleRepository.findByName(StaticRoles.USER));
        roles.add(roleRepository.findByName(StaticRoles.ADMIN));
        roles.add(roleRepository.findByName(StaticRoles.SUPER_ADMIN));
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
        roles.add(roleRepository.findByName(StaticRoles.USER));
        roles.add(roleRepository.findByName(StaticRoles.ADMIN));
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
        roles.add(roleRepository.findByName(StaticRoles.USER));
        root.setRoles(roles);
        appUserRepository.save(root);
    }
}