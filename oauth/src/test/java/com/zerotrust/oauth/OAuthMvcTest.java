package com.zerotrust.oauth;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zerotrust.oauth.service.OAuth2ClientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;


import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;


@Slf4j
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("dev")
public class OAuthMvcTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OAuth2ClientService clientService;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private static final String CLIENT_SECRET = UUID.randomUUID().toString();
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
    }


    @Test()
    public void testObtainAccessToken() throws Exception {

        clientService.getClient(CLIENT_ID).ifPresentOrElse(oAuth2Client -> {
            log.info("got the client id " + oAuth2Client);
            log.info("the scopes are " + oAuth2Client.getScopes());
            log.info("the grants are " + oAuth2Client.getGrants());
        }, () -> {
            log.error("creating the client id");
            clientService.createRestClient(CLIENT_ID, CLIENT_SECRET);
        });

        String username = "admin";
        String password = "nimda";
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);


        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));


        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String value = jsonParser.parseMap(resultString).get("access_token").toString();

        Assert.assertNotNull(value);
    }

}