package com.zerotrust.oauth.service;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.EmailPassword;
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
        String password = "I am a encryptedPassword";

        User user = service.createAdmin(email, password).orElseThrow(RuntimeException::new);
        Assert.assertEquals(email, user.getEmail());
        Assert.assertTrue(passwordEncoder.matches(password, user.getEncryptedPassword()));

        Assert.assertNotNull(service.getUser(email));
        Assert.assertEquals(user.getRoles(), service.getUser(email).get().getRoles());
    }

    @Test
    public void testCreateUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        String password = "I am a encryptedPassword";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        User user = service.createUser(email, password, roles).orElseThrow(RuntimeException::new);
        Assert.assertEquals(email, user.getEmail());
        Assert.assertTrue(passwordEncoder.matches(password, user.getEncryptedPassword()));

        Assert.assertNotNull(service.getUser(email).get());
        Assert.assertEquals(user.getRoles(), service.getUser(email).get().getRoles());
    }

    @Test
    public void testAsUserDetails() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        String password = "I am a encryptedPassword";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        User user = service.createUser(email, password, roles).orElseThrow(RuntimeException::new);
        UserDetails details = user.asUserDetails();

        Assert.assertEquals(user.getEmail(), details.getUsername());
        Assert.assertEquals(user.getEncryptedPassword(), details.getPassword());
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

        EmailPassword dto = service.createUser(email, roles).orElseThrow(RuntimeException::new);
        Assert.assertEquals(email, dto.getEmail());
        Assert.assertFalse(dto.getUnencryptedPassword().isEmpty());

        User user = service.getUser(email).orElseThrow(RuntimeException::new);
        Assert.assertTrue(passwordEncoder.matches(dto.getUnencryptedPassword(), user.getEncryptedPassword()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        String email = UUID.randomUUID().toString() + "!@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        service.createUser(email, roles).orElseThrow(RuntimeException::new);
        Assert.assertTrue(service.deleteUser(email));
        Assert.assertFalse(service.getUser(email).isPresent());
    }

    @Test
    public void testResetUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        EmailPassword first = service.createUser(email, roles).orElseThrow(RuntimeException::new);
        EmailPassword second = service.resetUser(email).orElseThrow(RuntimeException::new);

        Assert.assertNotEquals(first.getUnencryptedPassword(), second.getUnencryptedPassword());
        Assert.assertEquals(first.getEmail(), second.getEmail());

        User user = service.getUser(email).orElseThrow(RuntimeException::new);
        Assert.assertTrue(passwordEncoder.matches(second.getUnencryptedPassword(), user.getEncryptedPassword()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        String email = UUID.randomUUID().toString() + "@localhost.com";
        Role[] roles = new Role[] {
                new Role("admin")
        };

        service.createUser(email, roles).orElseThrow(RuntimeException::new);
        User user = service.updateUser(email, new Role[] {
                new Role("agents_read")
        }).orElseThrow(RuntimeException::new);
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertTrue(user.getRoles().contains(new Role("agents_read")));
    }

    @Test
    public void testGeneratePassword() {
        Assert.assertFalse(service.generatePassword().isEmpty());
    }

}
