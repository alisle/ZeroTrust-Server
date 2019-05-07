package com.zerotrust.links.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zerotrust.links.LinksServerApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinksServerApplication.class)
@ActiveProfiles("test")
public class AgentDTOTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJSON() throws Exception {
        String json = "{\n" +
                "\t\"uuid\": \"7637efe6-fcbb-4d19-aa62-973b2461e634\",\n" +
                "\t\"name\": \"unknown\",\n" +
                "\t\"interfaces\": [\"192.168.1.35\"]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        AgentOnlineDTO dto = mapper.readValue(json, AgentOnlineDTO.class);

        Assert.assertEquals("7637efe6-fcbb-4d19-aa62-973b2461e634", dto.getId().toString());
        Assert.assertEquals("unknown", dto.getName());
        Assert.assertEquals(1, dto.getInterfaces().length);
        Assert.assertEquals("192.168.1.35", dto.getInterfaces()[0]);
    }

    @Test
    public void testDTOToAgent() throws Exception {
        AgentOnlineDTO dto = new AgentOnlineDTO();
        dto.setId(UUID.randomUUID());
        dto.setName("Hello");
        dto.setInterfaces(new String[] {
                "192.168.0.1",
                "192.168.0.2"
        });


    }
}
