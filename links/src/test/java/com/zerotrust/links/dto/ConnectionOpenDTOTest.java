package com.zerotrust.links.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zerotrust.links.LinksServerApplication;
import com.zerotrust.model.Protocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinksServerApplication.class)
@ActiveProfiles("test")
public class ConnectionOpenDTOTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJSON() throws Exception {
        String json = "{\n" +
                "            \"uuid\":\"b2f0281d-da73-4116-8639-8a1c693511b0\",\n" +
                "            \"agent\":\"b15da2a9-67dd-446c-82ce-9512174bc16f\",\n" +
                "            \"hash\" : 950265093776986234,\n" +
                "            \"timestamp\" : \"2018-10-22T10:40:34.763563458+00:00\",\n" +
                "            \"protocol\" : \"TCP\",\n" +
                "            \"source\" : \"172.16.144.102\",\n" +
                "            \"destination\" : \"104.197.3.80\",\n" +
                "            \"source_port\" : 59325,\n" +
                "            \"destination_port\" : 80,\n" +
                "            \"username\" : \"root\",\n" +
                "            \"uid\" : 0,\n" +
                "            \"program_details\" : {\n" +
                "              \"inode\" : 631905,\n" +
                "              \"pid\" : 656,\n" +
                "              \"process_name\" : \"NetworkManager\",\n" +
                "              \"command_line\" : [\n" +
                "                \"/usr/sbin/NetworkManager\",\n" +
                "                \"--no-daemon\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ConnectionOpenDTO dto = mapper.readValue(json, ConnectionOpenDTO.class);

        Assert.assertEquals("b2f0281d-da73-4116-8639-8a1c693511b0", dto.getId().toString());
        Assert.assertEquals(950265093776986234L, dto.getHash());
        Assert.assertEquals("b15da2a9-67dd-446c-82ce-9512174bc16f", dto.getAgent().toString());
        Assert.assertEquals("2018-10-22T10:40:34.763563458Z", dto.getTimestamp().toString());
        Assert.assertEquals(Protocol.TCP, dto.getProtocol());
        Assert.assertEquals("172.16.144.102", dto.getSource());
        Assert.assertEquals("104.197.3.80", dto.getDestination());
        Assert.assertEquals(59325, dto.getSourcePort());
        Assert.assertEquals(80, dto.getDestinationPort());
        Assert.assertEquals("root", dto.getUsername());
        Assert.assertEquals(0, dto.getUid());

        Assert.assertEquals(631905, dto.getProgram().getInode());
        Assert.assertEquals(656, dto.getProgram().getPid());
        Assert.assertEquals("NetworkManager", dto.getProgram().getProccessName());
        Assert.assertArrayEquals(new String[] { "/usr/sbin/NetworkManager", "--no-daemon" }, dto.getProgram().getCommandLine());

    }
}
