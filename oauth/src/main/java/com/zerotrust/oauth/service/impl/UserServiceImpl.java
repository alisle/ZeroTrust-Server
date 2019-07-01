package com.zerotrust.oauth.service.impl;

import com.zerotrust.oauth.exception.NoSuchUserException;
import com.zerotrust.oauth.exception.UserAlreadyExistsException;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.CreatedUserDTO;
import com.zerotrust.oauth.repository.UserRepository;
import com.zerotrust.oauth.service.ConfigService;
import com.zerotrust.oauth.service.UserService;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.passay.CharacterCharacteristicsRule.ERROR_CODE;


@Service
public class UserServiceImpl implements UserService  {
    @Value("${com.zerotrust.oauth.admin-email}")
    private String admin_email;

    @Value("${com.zerotrust.oauth.admin-password}")
    private String admin_password;

    private final ConfigService configService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CharacterRule passwordRules[];
    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    public UserServiceImpl(ConfigService configService, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.configService = configService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.passwordRules = makeRuleSets();
    }

    private CharacterRule[] makeRuleSets() {
        CharacterRule[] rules = new CharacterRule[4];

        rules[0] = new CharacterRule(EnglishCharacterData.LowerCase);
        rules[1] = new CharacterRule(EnglishCharacterData.UpperCase);
        rules[2] = new CharacterRule(EnglishCharacterData.Digit);
        rules[3] = new CharacterRule(EnglishCharacterData.Special);

        Arrays.stream(rules).forEach(x -> {
            x.setNumberOfCharacters(4);
        });


        return rules;
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
        return createUser(email, password, new Role[] {
                new Role( "admin"),
                new Role("agents_read"),
                new Role("connections_read"),
                new Role( "networks_read"),
                new Role("networks_write")
        });
    }

    @Override
    public Optional<CreatedUserDTO> createUser(String email, Role[] roles) throws UserAlreadyExistsException {
        if(getUser(email).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String password = generatePassword();
        if(createUser(email, password, roles).isPresent()) {
            return Optional.of(new CreatedUserDTO(email, password));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteUser(String email) throws NoSuchUserException {
        getUser(email).orElseThrow(() -> new NoSuchUserException());
        repository.deleteById(email);

        return true;
    }

    @Override
    public Optional<CreatedUserDTO> resetUser(String email) throws NoSuchUserException {
        String password = generatePassword();
        User user = getUser(email).orElseThrow(() -> new NoSuchUserException());

        user.setPassword(passwordEncoder.encode(password));
        user = repository.save(user);

        if(user == null) {
            return Optional.empty();
        } else {
            return Optional.of(new CreatedUserDTO(email, password));
        }
    }

    @Override
    public Optional<User> updateUser(String email, Role[] roles) throws NoSuchUserException {
        User user = getUser(email).orElseThrow(() -> new NoSuchUserException());
        user.setRoles(Arrays.stream(roles).collect(Collectors.toSet()));
        user = repository.save(user);
        return Optional.ofNullable(user);
    }
    

    @Override
    public String generatePassword() {
        String password = passwordGenerator.generatePassword(24, passwordRules[0], passwordRules[1], passwordRules[2], passwordRules[3]);
        return password;
    }

    @EventListener
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if(this.configService.isFirstRun()) {
            createAdmin(admin_email, admin_password);
        }
    }
}
