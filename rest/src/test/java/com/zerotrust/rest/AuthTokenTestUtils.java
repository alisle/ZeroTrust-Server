package com.zerotrust.rest;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@ActiveProfiles("test")
public class AuthTokenTestUtils {
    public String grabAccessToken(MockMvc mockMvc) throws Exception {

        final String CLIENT_ID = "test";
        final String CLIENT_SECRET = "secret";
        final String USERNAME = "testAdmin";
        final String PASSWORD  = "password";
        final String CONTENT_TYPE = "application/json;charset=UTF-8";


        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", USERNAME);
        params.add("password", PASSWORD);


        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));


        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String value = jsonParser.parseMap(resultString).get("access_token").toString();

        return value;
    }
}
