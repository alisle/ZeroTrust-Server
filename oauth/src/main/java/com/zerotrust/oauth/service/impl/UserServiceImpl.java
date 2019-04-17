package com.zerotrust.oauth.service.impl;

import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.repository.UserRepository;
import com.zerotrust.oauth.service.ConfigService;
import com.zerotrust.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService  {
    @Value("${environment.ZEROTRUST_ADMIN_EMAIL:admin@localhost.com}")
    private String admin_email;

    @Value("${environment.ZEROTRUST_ADMIN_PASSWORD:secret}")
    private String admin_password;

    private final ConfigService configService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(ConfigService configService, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.configService = configService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUser(String email) {
        return repository.findById(email);
    }

    @Override
    public Optional<User> createUser(String email, String password, Role[] roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new HashSet<>(Arrays.asList(roles)));

        return Optional.ofNullable(repository.save(user));
    }

    @Override
    public Optional<User> createAdmin(String email, String password) {
        return createUser(email, password, new Role[] { new Role( "admin"), new Role("read"), new Role("write")});
    }

    @EventListener
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if(this.configService.isFirstRun()) {
            createAdmin(admin_email, admin_password);
        }
    }
}
