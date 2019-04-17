package com.zerotrust.rest.service;

import com.google.common.net.InetAddresses;
import com.zerotrust.rest.AuthTokenTestUtils;
import com.zerotrust.rest.ServerApplication;
import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_test_data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_test_data.sql")
})
public class AgentServiceImplTest {

    @Autowired
    AgentService service;


    @Autowired
    ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUnknownWithoutName() {
        UUID uuid = UUID.randomUUID();
        Agent agent = service.unknown(uuid);

        Assert.assertEquals(uuid, agent.getId());
        Assert.assertEquals(false, agent.getKnown());
        Assert.assertNotNull(agent.getFirstSeen());
        Assert.assertFalse(agent.getAlive());
        Assert.assertNull(agent.getName());

        Assert.assertNotNull(service.get(uuid));
    }


    @Test
    public void testUnknownWithName() {
        UUID uuid = UUID.randomUUID();
        String name = "UnknownWithName";
        Agent agent = service.unknown(uuid, name);

        Assert.assertEquals(uuid, agent.getId());
        Assert.assertEquals(false, agent.getKnown());
        Assert.assertNotNull(agent.getFirstSeen());
        Assert.assertFalse(agent.getAlive());
        Assert.assertEquals(name, agent.getName());
        Assert.assertNotNull(service.get(uuid));
    }

    @Test
    public void testSeenUnknown() {
        UUID uuid = UUID.randomUUID();
        service.seen(uuid);
        Agent agent = service.get(uuid).orElseThrow(() -> new RuntimeException("Oh Dear"));
        Assert.assertEquals(uuid, agent.getId());
    }

    @Test
    public void testOnline() {
        String name = "TestOnline";
        UUID uuid = UUID.randomUUID();
        Agent agent = service.online(uuid, name);
        Agent newAgent = service.get(agent.getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        Assert.assertEquals(agent.getName(), newAgent.getName());
        Assert.assertEquals(agent.getId(), uuid);
        Assert.assertEquals(true, agent.getKnown());
        Assert.assertEquals(true, agent.getAlive());
    }

    @Test
    public void testOffline() throws Exception {
        String name = "TestOffline";
        UUID uuid = UUID.randomUUID();
        service.online(uuid, name);
        service.offline(uuid);

        Agent agent = service.get(uuid).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(true, agent.getKnown());
        Assert.assertEquals(false, agent.getAlive());
    }

    @Test(expected = AgentNotFoundException.class)
    public void testOfflineNotFound() throws Exception {
        service.offline(UUID.randomUUID());
    }

    @Test(expected = AgentNotFoundException.class)
    public void testUpdateIPsNotFound() throws Exception {
        service.updateIPs(UUID.randomUUID(), null);
    }

    @Test
    public void testUpdateIPs() throws Exception {
        String name = "IP Update";
        UUID uuid = UUID.randomUUID();
        Set<IPAddress> firstTemplate = new HashSet<>();
        Set<IPAddress> secondTemplate = new HashSet<>();


        firstTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.1")), "192.168.0.1", IPAddress.Version.V4));
        firstTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.2")), "192.168.0.2", IPAddress.Version.V4));
        firstTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.3")), "192.168.0.3", IPAddress.Version.V4));
        firstTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.4")), "192.168.0.4", IPAddress.Version.V4));


        secondTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.5")),"192.168.0.5", IPAddress.Version.V4));
        secondTemplate.add(new IPAddress(InetAddresses.coerceToInteger(InetAddresses.forString("192.168.0.6")),"192.168.0.6", IPAddress.Version.V4));

        service.online(uuid, name);
        service.updateIPs(uuid, firstTemplate.toArray(new IPAddress[firstTemplate.size()]));


        Agent agent = service.get(uuid).orElseThrow(() -> new RuntimeException());
        Set<IPAddress> addresses = agent.getAddresses();

        Assert.assertEquals(4, addresses.size());
        for( IPAddress address : addresses) {
            Assert.assertTrue(firstTemplate.contains(address));
            firstTemplate.remove(address);
        }

        service.updateIPs(uuid, secondTemplate.toArray(new IPAddress[secondTemplate.size()]));
        agent = service.get(uuid).orElseThrow(() -> new RuntimeException());
        addresses = agent.getAddresses();
        Assert.assertEquals(2, addresses.size());

        for( IPAddress address : addresses) {
            Assert.assertTrue(secondTemplate.contains(address));
            firstTemplate.remove(address);
        }

    }



    @Test
    public void testSourceUserCount() throws Exception {
        List<UserCount> count = service.sourceUsersCount(UUID.fromString("afdbbc76-5dc9-452c-a248-45cb5aa0f769"));
        Assert.assertEquals(4, count.size());
    }

    @Test
    public void testDestinationCount() throws Exception {
        List<UserCount> count = service.destinationUsersCount(UUID.fromString("afdbbc76-5dc9-452c-a248-45cb5aa0f769"));
        Assert.assertEquals(2, count.size());
    }

    @Test
    public void testAllUsersCount() throws Exception {
        List<UserCount> count = service.allUsersCount(UUID.fromString("afdbbc76-5dc9-452c-a248-45cb5aa0f769"));
        Assert.assertEquals(6, count.size());
    }

    @Test
    public void testSourceProcessCount() throws Exception {
        List<ProcessCount> count = service.sourceProcessCount(UUID.fromString("afdbbc76-5dc9-452c-a248-45cb5aa0f769"));
        Assert.assertEquals(2, count.size());
    }

    @Test
    public void testDestinationProcessCount() throws Exception {
        List<ProcessCount> count = service.destinationProcessCount(UUID.fromString("afdbbc76-5dc9-452c-a248-45cb5aa0f769"));
        Assert.assertEquals(2, count.size());
    }

    @Test
    public void testCountIncomingConnections() throws Exception {
        List<AgentCount> count = service.countIncomingConnections();
        Assert.assertEquals(11, count.size());
    }

    @Test
    public void testCountOutgoingConnections() throws Exception {
        List<AgentCount> count = service.countOutgoingConnections();
        Assert.assertEquals(9, count.size());
    }

}