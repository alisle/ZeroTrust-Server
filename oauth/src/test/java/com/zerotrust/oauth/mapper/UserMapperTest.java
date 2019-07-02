package com.zerotrust.oauth.mapper;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.UserDetailsDTO;
import com.zerotrust.oauth.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("dev")
public class UserMapperTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @Test
    public void testUserToUserDetailsDTO() {
        User user = userService.createAdmin(UUID.randomUUID() + "@localhost.com", "I am a very crappy encryptedPassword").orElseThrow(RuntimeException::new);

        user.setExpired(false);
        user.setLocked(true);
        user.setCredentialsExpired(false);
        user.setEnabled(true);

        UserDetailsDTO dto =  mapper.userToUserDetailsDTO(user);

        Assert.assertEquals(user.getEmail(), dto.getEmail());
        Assert.assertEquals(user.isExpired(), dto.isExpired());
        Assert.assertEquals(user.isLocked(), dto.isLocked());
        Assert.assertEquals(user.isCredentialsExpired(), dto.isCredentailsExpired());
        Assert.assertEquals(user.isEnabled(), dto.isEnabled());
        Assert.assertEquals(user.getRoles().size(), dto.getRoles().size());

        user.getRoles().forEach(x -> {
            Assert.assertTrue(dto.getRoles().contains(x.getName()));
        });
    }
}
