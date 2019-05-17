package com.zerotrust.rest.mapper;

import com.zerotrust.model.entity.Network;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.dto.NetworkDTO;
import com.zerotrust.rest.exception.InvalidNetworkException;
import com.zerotrust.rest.exception.InvalidNetworkMaskException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@ActiveProfiles("test")
public class NetworkMapperTest {

    @Autowired
    private NetworkMapper mapper;

    private NetworkDTO generate() {
        NetworkDTO dto = new NetworkDTO();
        dto.setName("I am a name");
        dto.setDescription("I am a description");
        dto.setNetwork("192.168.0.1");
        dto.setMask("255.255.240.0");

        return dto;
    }

    @Test(expected = InvalidNetworkException.class)
    public void testNullName() throws Exception {
        NetworkDTO dto = generate();
        dto.setNetwork(null);

        mapper.convert(dto);
    }

    @Test(expected = InvalidNetworkException.class)
    public void testNullAddressString() throws Exception {
        NetworkDTO dto = generate();
        dto.setNetwork(null);

        mapper.convert(dto);
    }

    @Test(expected = InvalidNetworkException.class)
    public void testNullMaskString() throws Exception {
        NetworkDTO dto = generate();
        dto.setMask(null);

        mapper.convert(dto);
    }

    @Test(expected = InvalidNetworkMaskException.class)
    public void testInvalidIPAddress() throws Exception {
        NetworkDTO dto = generate();
        dto.setNetwork("I am not a valid IP address");

        mapper.convert(dto);
    }

    @Test(expected = InvalidNetworkMaskException.class)
    public void testInvalidMaskAddress() throws Exception {
        NetworkDTO dto = generate();
        dto.setMask("I too am not a valid IP address");

        mapper.convert(dto);
    }

    @Test
    public void testConvert() throws Exception {
        NetworkDTO dto = generate();
        Network network = mapper.convert(dto);

        Assert.assertEquals(dto.getName(), network.getName());
        Assert.assertEquals(dto.getDescription(), network.getDescription());
        Assert.assertEquals(dto.getNetwork(), network.getAddressString());
        Assert.assertEquals(dto.getMask(), network.getMaskString());
    }




}
