package com.zerotrust.rest.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.CreationUtils;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.model.Agent;
import com.zerotrust.rest.model.dto.AgentOnlineDTO;
import com.zerotrust.rest.model.dto.ConnectionOpenDTO;
import com.zerotrust.rest.model.dto.UpdateInterfacesDTO;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private AgentService service;

    @Autowired
    private ConnectionService connectionService;



    private MockMvc mockMvc;

    @Autowired
    AuthTokenTestUtils authTokenTestUtils;

    private String authToken;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

        authToken = authTokenTestUtils.grabAccessToken(mockMvc);
    }

    @Test
    public void testOnlineOffline() throws Exception {
        String json = "{ \"uuid\": \"2608cd33-740c-45d1-ab17-6d0d0bec44f9\", \"name\": \"TestWork\" }";

        MvcResult result = mockMvc.perform(post("/agents/online")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Agent agent = service.get(UUID.fromString("2608cd33-740c-45d1-ab17-6d0d0bec44f9")).orElseThrow(() -> new RuntimeException("oh dear"));
        Assert.assertEquals(true, agent.getKnown());
        Assert.assertEquals(true, agent.getAlive());

        result = mockMvc.perform(delete("/agents/offline/2608cd33-740c-45d1-ab17-6d0d0bec44f9")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        agent = service.get(UUID.fromString("2608cd33-740c-45d1-ab17-6d0d0bec44f9")).orElseThrow(() -> new RuntimeException("oh dear"));
        Assert.assertEquals(true, agent.getKnown());
        Assert.assertEquals(false, agent.getAlive());

    }

    @Test
    public void testUpdateInterfaces() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        AgentOnlineDTO agentdto = new AgentOnlineDTO();
        agentdto.setName("Agent");
        agentdto.setId(UUID.randomUUID());
        String agentJSON = objectMapper.writeValueAsString(agentdto);

        UpdateInterfacesDTO interfacesDTO = new UpdateInterfacesDTO();
        interfacesDTO.setInterfaces(new String[] {
                "192.168.0.1",
                "192.168.0.2"
        });
        String interfaceJSON = objectMapper.writeValueAsString(interfacesDTO);

        MvcResult result = mockMvc.perform(post("/agents/online")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(agentJSON))
                .andExpect(status().isOk())
                .andReturn();

        result = mockMvc.perform(post("/agents/" + agentdto.getId().toString() + "/interfaces")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(interfaceJSON))
                .andExpect(status().isOk())
                .andReturn();

        Agent agent  = service.get(agentdto.getId()).orElseThrow(() -> new RuntimeException("oh dear"));
        Assert.assertEquals(2, agent.getAddresses().size());
    }


    @Test
    public void testUsersSourceWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/users-source");
    }

    @Test
    public void testUsersDestinationWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/users-destination");
    }

    @Test
    public void testUsersWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/users");
    }

    @Test
    public void testProcessesSourceWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/processes-source");
    }

    @Test
    public void testProcessesDestinationWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/processes-destination");
    }

    @Test
    public void testCountIncomingConnectionsWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/count-incoming-connections");
    }

    @Test
    public void testCountOutgoingConnectionsWithAuthentication() throws Exception {
        testEndpointAuthentication("/agents/search/count-outgoing-connections");
    }




    @Test
    public void testUsersSourceWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/users-source");
    }

    @Test
    public void testUsersDestinationWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/users-destination");
    }

    @Test
    public void testUsersWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/users");
    }

    @Test
    public void testProcessesSourceWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/processes-source");
    }

    @Test
    public void testProcessesDestinationWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/processes-destination");
    }

    @Test
    public void testCountIncomingConnectionsWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/count-incoming-connections");
    }

    @Test
    public void testCountOutgoingConnectionsWithNoAuthentication() throws Exception {
        testEndpointNoAuthentication("/agents/search/count-outgoing-connections");
    }

    private void testEndpointAuthentication(String endpoint) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");

        MvcResult result = mockMvc.perform(get(endpoint)
                .header("Authorization", "Bearer " + authToken)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void testEndpointNoAuthentication(String endpoint) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("agent_id", "afdbbc76-5dc9-452c-a248-45cb5aa0f769");

        MvcResult result = mockMvc.perform(get(endpoint)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();

    }
}
