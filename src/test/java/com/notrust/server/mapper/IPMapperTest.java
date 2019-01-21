package com.notrust.server.mapper;

import com.notrust.server.ServerApplication;
import com.notrust.server.exception.InvalidIPAddress;
import com.notrust.server.model.IPAddress;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class IPMapperTest {

    @Autowired
    private IPMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertStringSuccess() throws Exception {
        IPAddress address = mapper.convertString("192.168.0.1");
        Assert.assertEquals("192.168.0.1", address.getAddressString());
        Assert.assertEquals(IPAddress.Version.V4, address.getVersion());
    }


    @Test(expected = InvalidIPAddress.class)
    public void testConvertStringFailure() throws Exception {
        IPAddress address = mapper.convertString("I am invalid IP");
    }

    @Test
    public void testConvertStringArraySuccess() throws Exception {
        String[] strings = new String[] {
                "192.168.0.1",
                "192.168.0.2",
                "192.168.0.3"
        };

        IPAddress[] addresses = mapper.convertStrings(strings);
        Assert.assertEquals(strings.length, addresses.length);

        for(int x = 0; x < addresses.length; x++) {
            Assert.assertEquals(addresses[x].getAddressString(), strings[x]);
        }
    }

    @Test(expected = InvalidIPAddress.class)
    public void testConvertStringArrayFailure() throws Exception {
        String[] strings = new String[] {
                "192.168.0.1",
                "192.168.0.2",
                "I am invalid",
                "192.168.0.3",
        };

        IPAddress[] addresses = mapper.convertStrings(strings);
    }
}
