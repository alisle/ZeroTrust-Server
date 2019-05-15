package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.ServerApplication;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ServerApplication.class)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_test_data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_test_data.sql")
})
public class ViewConnectionLinkControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    private String authToken;
    @Autowired
    AuthTokenTestUtils authTokenTestUtils;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = authTokenTestUtils.setup(webApplicationContext, springSecurityFilterChain);
        this.authToken = authTokenTestUtils.grabAccessToken();
    }

    @Test
    public void testGetLinkAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/connection_links/f70006c8-aa7e-43bb-a9f7-a16ff1e216f1", null);
    }

    @Test
    public void testGetLinkUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/f70006c8-aa7e-43bb-a9f7-a16ff1e216f1", null);
    }

    @Test
    public void testAllAuthenticated() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/api/connection_links", null);
    }

    @Test
    public void testAllUnauthenticated() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links", null);
    }

    @Test
    public void testAgentConnectionsAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");

        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/agent-connections", params);
    }

    @Test
    public void testAgentConnectionsUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");

        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/agent-connections", params);
    }

    @Test
    public void testGetLinksBetweenSourceAgentAndDestinationAgentAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");

        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/source-destination-agent-id", params);

    }

    @Test
    public void testGetLinksBetweenSourceAgentAndDestinationAgentUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");

        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/source-destination-agent-id", params);

    }

    @Test
    public void testGetLinksBetweenSourceAgentAndDestinationIPAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        params.add("destination_address", "10.0.41.41");
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/source-agent-id-destination-ip", params);

    }

    @Test
    public void testGetLinksBetweenSourceAgentAndDestinationIPUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        params.add("destination_address", "10.0.41.41");
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/source-agent-id-destination-ip", params);

    }

    @Test
    public void testGetLinksBetweenSourceIPAndDestinationAgentAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_address", "10.0.102.64");
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/source-ip-destination-agent-id", params);

    }

    @Test
    public void testGetLinksBetweenSourceIPAndDestinationAgentUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_address", "10.0.102.64");
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/source-ip-destination-agent-id", params);
    }

    @Test
    public void testGetActiveLinksFromSourceAgentAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/active-source-agent-id", params);

    }

    @Test
    public void testGetActiveLinksFromSourceAgentUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("source_agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/active-source-agent-id", params);

    }


    @Test
    public void testGetActiveLinksFromDestinationAgentAuthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/active-destination-agent-id", params);

    }

    @Test
    public void testGetActiveLinksFromDestinationAgentUnauthenticated() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("destination_agent_id", "0aef9c8d-18f4-4623-b271-d576fe0467a6");
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/active-destination-agent-id", params);

    }

    @Test
    public void testGetAllActiveLinksAuthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/active", null);
    }

    @Test
    public void testGetAllActiveLinksUnauthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/active", null);
    }

    @Test
    public void testGetTotalLinksAuthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/total-connection-links", null);
    }

    @Test
    public void testGetTotalLinksUnauthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/total-connection-links", null);
    }


    @Test
    public void testGetTotalActiveLinksAuthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointAuthentication("/api/connection_links/search/total-alive-connection-links", null);
    }

    @Test
    public void testGetTotalActiveLinksUnauthenticated() throws Exception {
        MvcResult result = authTokenTestUtils.testEndpointNoAuthentication("/api/connection_links/search/total-alive-connection-links", null);
    }

}
