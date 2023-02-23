package ru.nsu.backend.security.appUser;


import java.util.List;

public interface AppUserService {
    AppUser createAppUser(AppUser account);
    AppUser findByUsername(String username);
    List<AppUser> getAccounts();
}