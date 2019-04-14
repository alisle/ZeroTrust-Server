package com.zerotrust.oauth.service;

import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String email);
    Optional<User> createUser(String email, String password, Role[] roles);
    Optional<User> createAdmin(String email, String password);
}
