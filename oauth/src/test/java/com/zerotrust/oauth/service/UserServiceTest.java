package com.zerotrust.oauth.service;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.CreatedUserDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("dev")
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateAdminUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        String password = "I am a password";

        User user = service.createAdmin(email, password).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertTrue(passwordEncoder.matches(password, user.getPassword()));

        Assert.assertNotNull(service.getUser(email));
        Assert.assertEquals(user.getRoles(), service.getUser(email).get().getRoles());
    }

    @Test
    public void testCreateUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        String password = "I am a password";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        User user = service.createUser(email, password, roles).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertTrue(passwordEncoder.matches(password, user.getPassword()));

        Assert.assertNotNull(service.getUser(email).get());
        Assert.assertEquals(user.getRoles(), service.getUser(email).get().getRoles());
    }

    @Test
    public void testAsUserDetails() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        String password = "I am a password";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        User user = service.createUser(email, password, roles).orElseThrow(() -> new RuntimeException());
        UserDetails details = user.asUserDetails();

        Assert.assertEquals(user.getEmail(), details.getUsername());
        Assert.assertEquals(user.getPassword(), details.getPassword());
        Assert.assertEquals(!user.isCredentialsExpired(), details.isCredentialsNonExpired());
        Assert.assertEquals(user.isEnabled(), details.isEnabled());
        Assert.assertEquals(!user.isExpired(), details.isAccountNonExpired());
        Assert.assertEquals(!user.isLocked(), details.isAccountNonLocked());
    }

    @Test
    public void testCreateUserWithoutPassword() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        CreatedUserDTO dto = service.createUser(email, roles).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(email, dto.getEmail());
        Assert.assertFalse(dto.getPassword().isEmpty());

        User user = service.getUser(email).orElseThrow(() -> new RuntimeException());
        Assert.assertTrue(passwordEncoder.matches(dto.getPassword(), user.getPassword()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        String email = UUID.randomUUID().toString() + "!@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        service.createUser(email, roles).orElseThrow(() -> new RuntimeException());
        Assert.assertTrue(service.deleteUser(email));
        Assert.assertFalse(service.getUser(email).isPresent());
    }

    @Test
    public void testResetUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        CreatedUserDTO first = service.createUser(email, roles).orElseThrow(() -> new RuntimeException());
        CreatedUserDTO second = service.resetUser(email).orElseThrow(() -> new RuntimeException());

        Assert.assertNotEquals(first.getPassword(), second.getPassword());
        Assert.assertEquals(first.getEmail(), second.getEmail());

        User user = service.getUser(email).orElseThrow(() -> new RuntimeException());
        Assert.assertTrue(passwordEncoder.matches(second.getPassword(), user.getPassword()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        service.createUser(email, roles).orElseThrow(() -> new RuntimeException());
        User user = service.updateUser(email, new Role[] {
                new Role("agents_read")
        }).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertTrue(user.getRoles().contains(new Role("agents_read")));
    }

    @Test
    public void testGeneratePassword() {
        Assert.assertFalse(service.generatePassword().isEmpty());
    }

}
