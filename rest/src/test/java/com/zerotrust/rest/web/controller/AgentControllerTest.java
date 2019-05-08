package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.repository.AgentRepository;
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
public class AgentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Autowired
    private AgentRepository agentRepository;

    private MockMvc mockMvc;

    @Autowired
    AuthTokenTestUtils authTokenTestUtils;

    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = authTokenTestUtils.setup(webApplicationContext, springSecurityFilterChain);
        authTokenTestUtils.grabAccessToken();
        params.add("agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
    }

    @Test
    public void testFindAllWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/agents", params);
    }

    @Test
    public void testTotalAliveAgentsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/agents/search/total-alive-agents", params);
    }

    @Test
    public void testTotalAgentsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/agents/search/total-agents", params);
    }

    @Test
    public void testUsersSourceWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/users-source", params);
    }

    @Test
    public void testUsersDestinationWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/users-destination", params);
    }

    @Test
    public void testUsersWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/users", params);
    }

    @Test
    public void testProcessesSourceWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/processes-source", params);
    }

    @Test
    public void testProcessesDestinationWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/processes-destination", params);
    }

    @Test
    public void testCountIncomingConnectionsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/count-incoming-connections", params);
    }

    @Test
    public void testCountOutgoingConnectionsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api//agents/search/count-outgoing-connections", params);
    }



    @Test
    public void testFindAllWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/agents", params);
    }

    @Test
    public void testTotalAliveAgentsWithNoAuthentication() throws Exception {
         authTokenTestUtils.testEndpointNoAuthentication("/api/agents/search/total-alive-agents", params);
    }

    @Test
    public void testTotalAgentsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/agents/search/total-agents", params);
    }


    @Test
    public void testUsersSourceWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/users-source", params);
    }

    @Test
    public void testUsersDestinationWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/users-destination", params);
    }

    @Test
    public void testUsersWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/users", params);
    }

    @Test
    public void testProcessesSourceWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/processes-source", params);
    }

    @Test
    public void testProcessesDestinationWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/processes-destination", params);
    }

    @Test
    public void testCountIncomingConnectionsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/count-incoming-connections", params);
    }

    @Test
    public void testCountOutgoingConnectionsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api//agents/search/count-outgoing-connections", params);
    }


}
