package com.notrust.server.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.notrust.server.ServerApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ConnectionCloseDTOTest {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJSON() throws Exception {
        String json = "{" +
                "            \"hash\" : 1334410269481100237,\n" +
                "            \"timestamp\" : \"2018-10-22T10:07:36.651838320+00:00\",\n" +
                "            \"protocol\" : \"TCP\",\n" +
                "            \"source\" : \"172.16.144.102\",\n" +
                "            \"destination\" : \"104.198.143.177\",\n" +
                "            \"source_port\" : 50351,\n" +
                "            \"destination_port\" : 80" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ConnectionCloseDTO closed = mapper.readValue(json, ConnectionCloseDTO.class);
        Assert.assertEquals(1334410269481100237L, closed.getHash());
        Assert.assertEquals("2018-10-22T10:07:36.651838320Z", closed.getTimestamp().toString());
        Assert.assertEquals("TCP", closed.getProtocol());
        Assert.assertEquals("172.16.144.102", closed.getSource());
        Assert.assertEquals("104.198.143.177", closed.getDestination());
        Assert.assertEquals(50351, closed.getSourcePort());
        Assert.assertEquals(80, closed.getDestinationPort());

    }
}
