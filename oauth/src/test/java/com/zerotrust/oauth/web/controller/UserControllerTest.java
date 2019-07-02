package com.zerotrust.oauth.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.EmailPassword;
import com.zerotrust.oauth.model.Role;
import com.zerotrust.oauth.model.dto.UserDetailsDTO;
import com.zerotrust.oauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = AuthorizationServerApplication.class)
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddUserSuccess() throws Exception {
        String json = "{ \"email\" : \"test_user_1@localhost.com\", \"roles\": [ \"admin\" ] }";
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        EmailPassword emailPassword = objectMapper.readValue(response, EmailPassword.class);
        Assert.assertNotNull(emailPassword.getUnencryptedPassword());
        Assert.assertEquals("test_user_1@localhost.com", emailPassword.getEmail());
    }

    @Test
    public void testGetUser() throws Exception {
        String email = UUID.randomUUID() + "@localhost.com";
        userService.createUser( email, "I am a password", new Role[] { new Role("admin") }).orElseThrow(RuntimeException::new);

        MvcResult result = mockMvc.perform(get("/users/" + email)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        UserDetailsDTO details = objectMapper.readValue(response, UserDetailsDTO.class);
        Assert.assertEquals(email, details.getEmail());
    }


    @Test
    public void testDeleteUser() throws Exception {
        String email = UUID.randomUUID() + "@localhost.com";
        userService.createUser(email, "I am a password", new Role[] { new Role("admin") }).orElseThrow(RuntimeException::new);
        mockMvc.perform(delete("/users/" + email)).andExpect(status().isOk());
        Assert.assertFalse(userService.getUser(email).isPresent());
    }

    @Test
    public void testResetUser() throws Exception {
        String email = UUID.randomUUID() + "@localhost.com";
        EmailPassword emailPassword = userService.createUser(email, new Role[] { new Role("admin")}).orElseThrow(RuntimeException::new);

        MvcResult result = mockMvc.perform(post("/users/reset/" + email))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        EmailPassword newEmailPassword = objectMapper.readValue(response, EmailPassword.class);

        Assert.assertNotEquals(emailPassword.getUnencryptedPassword(), newEmailPassword.getUnencryptedPassword());
    }


    @Test
    public void testUpdateUser() throws Exception {
        String email = UUID.randomUUID() + "@localhost.com";
        String json = "{ \"roles\" : [ \"agents_read\"] }";

        userService.createUser(email, "I am a passworD", new Role[] { new Role("admin") }).orElseThrow(RuntimeException::new);

        MvcResult result = mockMvc.perform(put("/users/" + email)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();


        String response  = result.getResponse().getContentAsString();
        UserDetailsDTO details = objectMapper.readValue(response, UserDetailsDTO.class);
        Assert.assertEquals(1, details.getRoles().size());
        Assert.assertTrue(details.getRoles().contains("agents_read"));
        Assert.assertFalse(details.getRoles().contains("admin"));
    }


}
