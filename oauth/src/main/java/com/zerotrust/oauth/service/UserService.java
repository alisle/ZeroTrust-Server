package com.zerotrust.oauth.service;

import com.zerotrust.oauth.exception.NoSuchUserException;
import com.zerotrust.oauth.exception.UserAlreadyExistsException;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.CreatedUserDTO;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String email);
    Optional<User> createUser(String email, String password, Role[] roles);
    Optional<User> createAdmin(String email, String password);
    Optional<CreatedUserDTO> createUser(String email, Role[] roles) throws UserAlreadyExistsException;
    boolean deleteUser(String email) throws NoSuchUserException;
    Optional<CreatedUserDTO> resetUser(String email) throws NoSuchUserException;
    Optional<User> updateUser(String email, Role[] roles) throws NoSuchUserException;

    String generatePassword();
}
