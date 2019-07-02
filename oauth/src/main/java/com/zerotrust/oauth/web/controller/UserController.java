package com.zerotrust.oauth.web.controller;

import com.zerotrust.oauth.exception.InvalidRoleException;
import com.zerotrust.oauth.exception.NoSuchUserException;
import com.zerotrust.oauth.exception.UserAlreadyExistsException;
import com.zerotrust.oauth.mapper.UserMapper;
import com.zerotrust.oauth.model.EmailPassword;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.NewUserDTO;
import com.zerotrust.oauth.model.dto.UpdateUserDTO;
import com.zerotrust.oauth.model.dto.UserDetailsDTO;
import com.zerotrust.oauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> get(@PathVariable("email") String email) throws NoSuchUserException {
        User user = userService.getUser(email).orElseThrow(NoSuchUserException::new);
        UserDetailsDTO dto = mapper.userToUserDetailsDTO(user);
        return new ResponseEntity<>(dto, null, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EmailPassword> post(@Valid @RequestBody NewUserDTO dto) throws UserAlreadyExistsException, InvalidRoleException {
        Role[] roles = Arrays.stream(dto.getRoles()).map(x -> new Role(x)).toArray(Role[]::new);
        EmailPassword emailPassword = userService.createUser(dto.getEmail(), roles).orElseThrow(UserAlreadyExistsException::new);
        return new ResponseEntity<>(emailPassword, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.DELETE, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("email") String email)  throws NoSuchUserException {
        userService.deleteUser(email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/reset/{email}", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EmailPassword> reset(@PathVariable("email") String email) throws NoSuchUserException {
        EmailPassword emailPassword = userService.resetUser(email).orElseThrow(NoSuchUserException::new);
        return new ResponseEntity<>(emailPassword, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.PUT, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> update(@PathVariable("email") String email, @Valid @RequestBody UpdateUserDTO dto) throws NoSuchUserException {
        Role[] roles = Arrays.stream(dto.getRoles()).map(x -> new Role(x)).toArray(Role[]::new);
        User user = userService.updateUser(email, roles).orElseThrow(NoSuchUserException::new);
        UserDetailsDTO details = mapper.userToUserDetailsDTO(user);
        return new ResponseEntity<>(details, null, HttpStatus.OK);
    }


}
