package com.notrust.server.service;

import com.notrust.server.CreationUtils;
import com.notrust.server.ServerApplication;
import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionCloseDTO;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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
        Assert.assertEquals(close.getTimestamp(), closeConnection.getStart());
        Assert.assertEquals(open.getTimestamp().toEpochMilli() - close.getTimestamp().toEpochMilli(), closeConnection.getDuration());
    }

    @Test
    public void testGet() {
        ConnectionOpenDTO open = CreationUtils.ConnectionNewDTO();
        Connection openConnection = service.open(open).orElseThrow(() -> new RuntimeException("oh dear"));
        Connection getConnection = service.get(openConnection.getId()).orElseThrow(() -> new RuntimeException("Oh dear"));

        Assert.assertEquals(openConnection.getId(), getConnection.getId());


    }
}

