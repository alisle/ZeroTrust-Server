package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.repository.NetworkRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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


    private MockMvc mockMvc;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    @Autowired
    AuthTokenTestUtils authTokenTestUtils;

    @Autowired
    NetworkRepository networkRepository;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = authTokenTestUtils.setup(webApplicationContext, springSecurityFilterChain);
        authTokenTestUtils.grabAccessToken();
        params.add("network_id", "59ed5983-da6a-4036-b2e9-9c613594ddb9");
    }

    @Test
    public void testPostNetwork() throws Exception {
        String json = "{ \"name\" : \"test_network\", \"description\" : \"I am a test network\", \"network\" : \"192.168.0.0\", \"mask\" : \"255.0.0.0\" }";
        authTokenTestUtils.authenticatedPost("/api/networks", json);
    }

    @Test
    public void testAllAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/networks", null);
    }

    @Test
    public void testAllUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/networks", null);
    }

    @Test
    public void testCountActiveDestinationConnectionsAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveDestinationConnectionsUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveSourceConnectionsAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/networks/search/count-active-destination-connections", params);
    }

    @Test
    public void testCountActiveSourceConnectionsUnAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/networks/search/count-active-destination-connections", params);
    }

}
