package com.zerotrust.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@ActiveProfiles("test")
public class AuthTokenTestUtils {

    private MockMvc mockMvc;
    private String authToken;

    public MockMvc setup(WebApplicationContext webApplicationContext, FilterChainProxy springSecurityFilterChain) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        return mockMvc;
    }

    public String grabAccessToken() throws Exception {

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
        authToken = jsonParser.parseMap(resultString).get("access_token").toString();

        return authToken;
    }

    public MvcResult testEndpointAuthentication(String endpoint, MultiValueMap<String, String> params ) throws Exception {

        if(params == null) {
            params = new LinkedMultiValueMap<>();
        }

        MvcResult result = mockMvc.perform(get(endpoint)
                .header("Authorization", "Bearer " + authToken)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        return result;
    }

    public MvcResult testEndpointNoAuthentication(String endpoint, MultiValueMap<String, String> params ) throws Exception {
        if(params == null) {
            params = new LinkedMultiValueMap<>();
        }

        MvcResult result = mockMvc.perform(get(endpoint)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();

        return result;
    }

}
