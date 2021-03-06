package com.zerotrust.links.service;

import com.zerotrust.links.CreationUtils;
import com.zerotrust.links.LinksServerApplication;
import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.model.entity.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinksServerApplication.class)
@ActiveProfiles("test")
public class ConnectionServiceImplTests {

    @Autowired
    ConnectionService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOpenWithNoUUID() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        open.setId(null);

        Optional<Connection> connection = service.open(open);
        Assert.assertFalse(connection.isPresent());
    }
    @Test
    public void testCloseWithNoUUID() {
        ConnectionCloseDTO close = CreationUtils.ConnectionCloseDTO();
        Assert.assertFalse(service.close(close).isPresent());
    }

    @Test
    public void testOpenWithoutClose() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        Optional<Connection> connection = service.open(open);
        Assert.assertTrue(connection.isPresent());
    }

    @Test
    public void testOpenWithClose() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        ConnectionCloseDTO close = CreationUtils.ConnectionCloseDTO();

        close.setId(open.getId());

        Connection openConnection = service.open(open).orElseThrow(RuntimeException::new);
        Connection closeConnection = service.close(close).orElseThrow(RuntimeException::new);

        Assert.assertEquals(open.getId(), closeConnection.getId());
        Assert.assertEquals(open.getTimestamp(), openConnection.getStart());
        Assert.assertEquals(close.getTimestamp(), closeConnection.getEnd());

        Assert.assertEquals("10.0.0.0", openConnection.getDestinationNetwork().getAddressString());
        Assert.assertEquals(close.getTimestamp().toEpochMilli() - open.getTimestamp().toEpochMilli(), closeConnection.getDuration());
    }

    @Test
    public void testGet() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        Connection openConnection = service.open(open).orElseThrow(RuntimeException::new);
        Connection getConnection = service.get(openConnection.getId()).orElseThrow(RuntimeException::new);

        Assert.assertEquals(openConnection.getId(), getConnection.getId());

    }

    @Test
    public void testGetAliveConnectionsSameAgent() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        ConnectionOpenDTO newOpen = CreationUtils.ConnectionNewDTO();
        ConnectionCloseDTO close = CreationUtils.ConnectionCloseDTO();

        close.setId(open.getId());
        close.setAgent(open.getAgent());
        newOpen.setAgent(open.getAgent());

        service.open(open).orElseThrow(RuntimeException::new);
        service.open(newOpen).orElseThrow(RuntimeException::new);
        service.close(close).orElseThrow(RuntimeException::new);

        List<Connection> connectionList = service.findAliveConnections(newOpen.getAgent());

        Assert.assertEquals(1, connectionList.size());
        Assert.assertEquals(newOpen.getId(), connectionList.get(0).getId());

    }

    @Test
    public void testGetAliveConnectionsDifferentAgent() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        ConnectionOpenDTO newOpen = CreationUtils.ConnectionNewDTO();
        ConnectionCloseDTO close = CreationUtils.ConnectionCloseDTO();

        close.setId(open.getId());

        service.open(open).orElseThrow(RuntimeException::new);
        service.open(newOpen).orElseThrow(RuntimeException::new);
        service.close(close).orElseThrow(RuntimeException::new);

        List<Connection> connectionList = service.findAliveConnections(newOpen.getAgent());

        Assert.assertEquals(1, connectionList.size());
        Assert.assertEquals(newOpen.getId(), connectionList.get(0).getId());

    }

    @Test
    public void testAliveConnections() throws Exception {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        service.open(open).orElseThrow(RuntimeException::new);
        List<Connection> connectionList = service.findAliveConnections(open.getAgent());

        Assert.assertEquals(1, connectionList.size());
        Assert.assertEquals(open.getId(), connectionList.get(0).getId());

    }

    @Test
    public void testValidConnections() throws Exception {
        Random random = new Random();
        UUID agent = UUID.randomUUID();
        Set<Long> hashes = new HashSet<>();
        Set<Long> killedHashes = new HashSet<>();
        Set<Long> aliveHashes = new HashSet<>();

        for(int connection = 0; connection < 100; connection++) {
            ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
            open.setAgent(agent);
            open.setHash(random.nextLong());
            hashes.add(open.getHash());

            if(connection % 2 == 0) {
                killedHashes.add(open.getHash());
            } else {
                aliveHashes.add(open.getHash());
            }

            service.open(open).orElseThrow(RuntimeException::new);
        }

        Assert.assertEquals(100, service.findAliveConnections(agent).size());

        service.validateConnections(agent, aliveHashes);
        Assert.assertEquals(50, service.findAliveConnections(agent).size());

        service.findAliveConnections(agent).stream().forEach(connection -> {
            Assert.assertFalse(killedHashes.contains(connection.getConnectionHash()));
        });
    }
}
