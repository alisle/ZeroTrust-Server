package com.notrust.server.web.controller;

import com.notrust.server.ServerApplication;
import com.notrust.server.model.Agent;
import com.notrust.server.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@SpringBootTest(classes = ServerApplication.class)
public class AgentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AgentService service;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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

}
