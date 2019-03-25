package com.zerotrust.server.service;

import com.zerotrust.server.CreationUtils;
import com.zerotrust.server.ServerApplication;
import com.zerotrust.server.model.Connection;
import com.zerotrust.server.model.dto.ConnectionCloseDTO;
import com.zerotrust.server.model.dto.ConnectionOpenDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ConnectionServiceImplTest {

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

        Connection openConnection = service.open(open).orElseThrow(() -> new RuntimeException("oh dear"));
        Connection closeConnection = service.close(close).orElseThrow(() -> new RuntimeException("oh dear"));

        Assert.assertEquals(open.getId(), closeConnection.getId());
        Assert.assertEquals(open.getTimestamp(), openConnection.getStart());
        Assert.assertEquals(close.getTimestamp(), closeConnection.getEnd());

        Assert.assertEquals(close.getTimestamp().toEpochMilli() - open.getTimestamp().toEpochMilli(), closeConnection.getDuration());
    }

    @Test
    public void testGet() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        Connection openConnection = service.open(open).orElseThrow(() -> new RuntimeException("oh dear"));
        Connection getConnection = service.get(openConnection.getId()).orElseThrow(() -> new RuntimeException("Oh dear"));

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

        service.open(open).orElseThrow(() -> new RuntimeException("oh dear"));
        service.open(newOpen).orElseThrow(() -> new RuntimeException("oh dear"));
        service.close(close).orElseThrow(() -> new RuntimeException("oh dear"));

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

        service.open(open).orElseThrow(() -> new RuntimeException("oh dear"));
        service.open(newOpen).orElseThrow(() -> new RuntimeException("oh dear"));
        service.close(close).orElseThrow(() -> new RuntimeException("oh dear"));

        List<Connection> connectionList = service.findAliveConnections(newOpen.getAgent());

        Assert.assertEquals(1, connectionList.size());
        Assert.assertEquals(newOpen.getId(), connectionList.get(0).getId());

    }

}

