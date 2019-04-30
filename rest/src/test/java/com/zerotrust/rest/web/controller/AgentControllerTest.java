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
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
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
    public void testFindAllWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents", params);
    }

    @Test
    public void testTotalAliveAgentsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/total-alive-agents", params);
    }

    @Test
    public void testTotalAgentsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/total-agents", params);
    }

    @Test
    public void testUsersSourceWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/users-source", params);
    }

    @Test
    public void testUsersDestinationWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/users-destination", params);
    }

    @Test
    public void testUsersWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/users", params);
    }

    @Test
    public void testProcessesSourceWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/processes-source", params);
    }

    @Test
    public void testProcessesDestinationWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/processes-destination", params);
    }

    @Test
    public void testCountIncomingConnectionsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/count-incoming-connections", params);
    }

    @Test
    public void testCountOutgoingConnectionsWithAuthentication() throws Exception {
        authTokenTestUtils.testEndpointAuthentication("/agents/search/count-outgoing-connections", params);
    }



    @Test
    public void testFindAllWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents", params);
    }

    @Test
    public void testTotalAliveAgentsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/total-alive-agents", params);
    }

    @Test
    public void testTotalAgentsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/total-agents", params);
    }


    @Test
    public void testUsersSourceWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/users-source", params);
    }

    @Test
    public void testUsersDestinationWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/users-destination", params);
    }

    @Test
    public void testUsersWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/users", params);
    }

    @Test
    public void testProcessesSourceWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/processes-source", params);
    }

    @Test
    public void testProcessesDestinationWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/processes-destination", params);
    }

    @Test
    public void testCountIncomingConnectionsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/count-incoming-connections", params);
    }

    @Test
    public void testCountOutgoingConnectionsWithNoAuthentication() throws Exception {
        authTokenTestUtils.testEndpointNoAuthentication("/agents/search/count-outgoing-connections", params);
    }


}
