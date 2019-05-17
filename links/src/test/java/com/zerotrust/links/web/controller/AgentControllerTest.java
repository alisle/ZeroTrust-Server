package com.zerotrust.links.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotrust.links.LinksServerApplication;
import com.zerotrust.links.dto.AgentOnlineDTO;
import com.zerotrust.links.dto.UpdateInterfacesDTO;
import com.zerotrust.model.entity.Agent;
import com.zerotrust.links.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = LinksServerApplication.class)
public class AgentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private AgentService service;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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


}
