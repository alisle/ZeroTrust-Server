package com.zerotrust.rest.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zerotrust.rest.ServerApplication;
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
public class UpdateInterfacesDTOTest {
    @Before
    public void setup() { MockitoAnnotations.initMocks(this); }

    @Test
    public void testJSON() throws Exception {
        String json = "{\n" +
                "\t\"interfaces\": [\"10.10.130.97\"]\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UpdateInterfacesDTO dto = mapper.readValue(json, UpdateInterfacesDTO.class);

        Assert.assertEquals(1, dto.getInterfaces().length);
        Assert.assertEquals("10.10.130.97", dto.getInterfaces()[0]);
    }
}
