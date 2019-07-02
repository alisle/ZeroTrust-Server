package com.zerotrust.oauth.service;

import com.zerotrust.oauth.exception.NoSuchUserException;
import com.zerotrust.oauth.exception.UserAlreadyExistsException;
import com.zerotrust.oauth.model.EmailPassword;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> list(Pageable pageable);
    Optional<User> getUser(String email);
    Optional<User> createUser(String email, String password, Role[] roles);
    Optional<User> createAdmin(String email, String password);
    Optional<EmailPassword> createUser(String email, Role[] roles) throws UserAlreadyExistsException;
    boolean deleteUser(String email) throws NoSuchUserException;
    Optional<EmailPassword> resetUser(String email) throws NoSuchUserException;
    Optional<User> updateUser(String email, Role[] roles) throws NoSuchUserException;

    String generatePassword();
}
