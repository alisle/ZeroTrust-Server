package com.notrust.server.web.controller;

import com.notrust.server.ServerApplication;
import com.notrust.server.model.Connection;
import com.notrust.server.service.ConnectionService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ConnectionControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ConnectionService service;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testOpenConnection() throws Exception {
        String json = "{\"hash\":-3742850389707695053,\"uuid\":\"2608cd33-740c-45d1-ab17-6d0d0bec44f9\",\"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\"timestamp\":\"2018-10-25T09:43:45.725796148+00:00\",\"protocol\":\"TCP\",\"sourceString\":\"172.16.144.102\",\"destinationString\":\"104.197.3.80\",\"source_port\":38487,\"destination_port\":80,\"username\":\"root\",\"uid\":0,\"program_details\":{\"inode\":775762,\"pid\":656,\"process_name\":\"NetworkManager\",\"command_line\":[\"/usr/sbin/NetworkManager\",\"--no-daemon\"]}}";

        MvcResult result = mockMvc.perform(post("/connections/open")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        service.get(UUID.fromString("2608cd33-740c-45d1-ab17-6d0d0bec44f9")).orElseThrow(() -> new RuntimeException("oh dear"));
    }

    @Test
    public void testCloseConnectionNoOpen() throws Exception {
        String json = "{\"hash\":-5948444490997452592,\"uuid\":null,\"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\"timestamp\":\"2018-10-25T12:40:40.767887192+00:00\",\"protocol\":\"TCP\",\"sourceString\":\"172.16.144.102\",\"destinationString\":\"104.198.143.177\",\"source_port\":59495,\"destination_port\":80}";
        MvcResult result = mockMvc.perform(post("/connections/close")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void testOpenConnectionAndClose() throws Exception {
        String json = "{\"hash\":-3742850389707695053,\"uuid\":\"2608cd33-740c-45d1-ab17-6d0d0bec44f9\",\"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\"timestamp\":\"2018-10-25T09:43:45.725796148+00:00\",\"protocol\":\"TCP\",\"sourceString\":\"172.16.144.102\",\"destinationString\":\"104.197.3.80\",\"source_port\":38487,\"destination_port\":80,\"username\":\"root\",\"uid\":0,\"program_details\":{\"inode\":775762,\"pid\":656,\"process_name\":\"NetworkManager\",\"command_line\":[\"/usr/sbin/NetworkManager\",\"--no-daemon\"]}}";
        MvcResult result = mockMvc.perform(post("/connections/open")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();


        json = "{\"hash\":-5948444490997452592,\"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\"uuid\":\"2608cd33-740c-45d1-ab17-6d0d0bec44f9\",\"timestamp\":\"2018-10-25T12:40:40.767887192+00:00\",\"protocol\":\"TCP\",\"sourceString\":\"172.16.144.102\",\"destinationString\":\"104.198.143.177\",\"source_port\":59495,\"destination_port\":80}";
        result = mockMvc.perform(post("/connections/close")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Connection connection = service.get(UUID.fromString("2608cd33-740c-45d1-ab17-6d0d0bec44f9")).orElseThrow(() -> new RuntimeException("oh dear"));
        Assert.assertNotNull(connection.getStart());
        Assert.assertNotNull(connection.getEnd());
        Assert.assertTrue(connection.getDuration() > 0);
    }
}
