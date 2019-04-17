package com.zerotrust.rest.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ProgramDTOTest {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJSON() throws Exception {
        String json = " {\n" +
                "              \"inode\" : 631905,\n" +
                "              \"pid\" : 656,\n" +
                "              \"process_name\" : \"NetworkManager\",\n" +
                "              \"command_line\" : [\n" +
                "                \"/usr/sbin/NetworkManager\",\n" +
                "                \"--no-daemon\"\n" +
                "              ]\n" +
                "            }\n";

        ObjectMapper mapper = new ObjectMapper();
        ProgramDTO program = mapper.readValue(json, ProgramDTO.class);
        Assert.assertEquals(631905, program.getInode());
        Assert.assertEquals(656, program.getPid());
        Assert.assertEquals("NetworkManager", program.getProccessName());
        Assert.assertArrayEquals(new String[] { "/usr/sbin/NetworkManager", "--no-daemon" }, program.getCommandLine());
    }

}