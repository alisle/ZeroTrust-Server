package com.zerotrust.oauth.service;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.User;
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




}
