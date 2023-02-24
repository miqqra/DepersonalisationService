package ru.nsu.backend.security.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.backend.exceptions.ResponseException;
import ru.nsu.backend.security.appUser.AppUserRepository;
import ru.nsu.backend.security.appUser.AppUserService;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class RoleServiceImpl {
    private final RoleRepository roleRepository;
    private final AppUserRepository userRepository;
    private final AppUserService userService;

    @Transactional
    public Role addRoleForUse(String userName, Role role) throws ResponseException {
        var user = userService.getUser(userName);
        if (user == null) {
            throw new IllegalStateException("No user");
        }
        userRepository.deleteById(user.getId());
        user.getRoles().add(role);
        userService.createAppUser(user);
        return null;
    }
}
