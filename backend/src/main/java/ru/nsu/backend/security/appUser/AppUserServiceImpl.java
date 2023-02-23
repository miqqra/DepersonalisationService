package ru.nsu.backend.security.appUser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.backend.security.role.Role;
import ru.nsu.backend.security.role.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public AppUser createAppUser(AppUser account) {
        account.setPassword(encoder.encode(account.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setRoles(roles);
        return accountRepository.save(account);
    }


    public AppUser createAppUserSuperAdmin(AppUser account) {
        account.setPassword(encoder.encode(account.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN"));
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_SUPER_USER"));
        account.setRoles(roles);
        return accountRepository.save(account);
    }

    @Override
    public AppUser findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAccounts() {
        return accountRepository.findAll();
    }
}