package com.zerotrust.rest.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.model.Protocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@ActiveProfiles("test")
public class ConnectionCloseDTOTest {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJSON() throws Exception {
        String json = "{" +
                "            \"uuid\":\"b2f0281d-da73-4116-8639-8a1c693511b0\",\n" +
                "            \"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\n" +
                "            \"hash\" : 1334410269481100237,\n" +
                "            \"timestamp\" : \"2018-10-22T10:07:36.651838320+00:00\",\n" +
                "            \"protocol\" : \"TCP\",\n" +
                "            \"sourceString\" : \"172.16.144.102\",\n" +
                "            \"destinationString\" : \"104.198.143.177\",\n" +
                "            \"source_port\" : 50351,\n" +
                "            \"destination_port\" : 80" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ConnectionCloseDTO closed = mapper.readValue(json, ConnectionCloseDTO.class);
        Assert.assertEquals("b2f0281d-da73-4116-8639-8a1c693511b0", closed.getId().toString());
        Assert.assertEquals(1334410269481100237L, closed.getHash());
        Assert.assertEquals("2018-10-22T10:07:36.651838320Z", closed.getTimestamp().toString());
        Assert.assertEquals(Protocol.TCP, closed.getProtocol());
        Assert.assertEquals("172.16.144.102", closed.getSource());
        Assert.assertEquals("104.198.143.177", closed.getDestination());
        Assert.assertEquals(50351, closed.getSourcePort());
        Assert.assertEquals(80, closed.getDestinationPort());
        Assert.assertEquals("b15da2a9-67dd-446c-82ce-9512174bc16f", closed.getAgent().toString());

    }
}
