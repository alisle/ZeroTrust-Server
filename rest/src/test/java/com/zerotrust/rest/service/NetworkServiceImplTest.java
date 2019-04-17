package com.zerotrust.rest.service;

import com.google.common.net.InetAddresses;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.model.Network;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class NetworkServiceImplTest {
    @Autowired
    NetworkService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testAutoGeneratedIPRanges() throws Exception {
        String[] ips = new String[] {
                "192.168.0.1",
                "10.1.1.1",
                "172.16.0.2"
        };

        for(int x = 0; x < ips.length; x++) {
            InetAddress inetAddress = InetAddress.getByName(ips[x]);
            int address = InetAddresses.coerceToInteger(inetAddress);
            Assert.assertEquals(1, service.findNetworks(address, false).size());
        }
    }

    @Test
    public void testFindNetworksHitWithoutInternet() throws Exception {
        Random random = new Random();
        for(int x = 0; x < 255; x++) {
            int rand = random.nextInt(255);

            InetAddress inetAddress = InetAddress.getByName("192.168." + x + "." + rand);
            int address = InetAddresses.coerceToInteger(inetAddress);

            List<Network> networks = service.findNetworks(address, false);

            Assert.assertEquals(1, networks.size());
            Assert.assertEquals("192.168.0.0", networks.get(0).getAddressString());
        }
    }

    @Test
    public void testFindNetworksMissWithoutInternet() throws Exception {
        Random random = new Random();
        for(int x = 0; x < 255; x++) {
            int rand = random.nextInt(255);

            InetAddress inetAddress = InetAddress.getByName("192.169." + x + "." + rand);
            int address = InetAddresses.coerceToInteger(inetAddress);

            List<Network> networks = service.findNetworks(address, false);

            Assert.assertEquals(0, networks.size());
        }
    }

    @Test
    public void testFindNetworksHitWithInternet() throws Exception {
        Random random = new Random();
        for(int x = 0; x < 255; x++) {
            int rand = random.nextInt(255);

            InetAddress inetAddress = InetAddress.getByName("192.168." + x + "." + rand);
            int address = InetAddresses.coerceToInteger(inetAddress);

            List<Network> networks = service.findNetworks(address, true);

            Assert.assertEquals(2, networks.size());
            Assert.assertEquals("192.168.0.0", networks.get(1).getAddressString());
            Assert.assertEquals("0.0.0.0", networks.get(0).getAddressString());
        }
    }

    @Test
    public void testFindNetworksMissWithInternet() throws Exception {
        Random random = new Random();
        for(int x = 0; x < 255; x++) {
            int rand = random.nextInt(255);

            InetAddress inetAddress = InetAddress.getByName("192.169." + x + "." + rand);
            int address = InetAddresses.coerceToInteger(inetAddress);

            List<Network> networks = service.findNetworks(address, true);
            Assert.assertEquals(1, networks.size());
            Assert.assertEquals("0.0.0.0", networks.get(0).getAddressString());
        }
    }

    @Test
    public void testFindMostRestrictiveNetork() throws Exception {
        InetAddress inetAddress = InetAddress.getByName("192.168.0.2");
        int address = InetAddresses.coerceToInteger(inetAddress);

        Network network = service.findMostRestrictiveNetwork(address);
        Assert.assertEquals("192.168.0.0", network.getAddressString());
    }
}