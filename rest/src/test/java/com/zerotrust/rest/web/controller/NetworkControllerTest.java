package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ServerApplication.class)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_test_data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_test_data.sql")
})
public class NetworkControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private NetworkService service;


    private MockMvc mockMvc;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    @Autowired
    AuthTokenTestUtils authTokenTestUtils;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = authTokenTestUtils.setup(webApplicationContext, springSecurityFilterChain);
        authTokenTestUtils.grabAccessToken();
        params.add("network_id", "59ed5983-da6a-4036-b2e9-9c613594ddb9");
    }

    @Test
    public void testAllAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/networks", null);
    }

    @Test
    public void testAllUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/networks", null);
    }

    @Test
    public void testCountActiveDestinationConnectionsAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveDestinationConnectionsUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveSourceConnectionsAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveSourceConnectionsUnAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/networks/search/count-active-destination-connections", params);
    }

}
