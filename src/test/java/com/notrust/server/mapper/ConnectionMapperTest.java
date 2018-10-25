package com.notrust.server.mapper;

import com.notrust.server.CreationUtils;
import com.notrust.server.ServerApplication;
import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ConnectionMapperTest {

    @Autowired
    private ConnectionMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOpenDTOConnection() {
        ConnectionOpenDTO dto = CreationUtils.ConnectionNewDTO();

        Connection connection = mapper.OpenDTOtoConnection(dto);
        Assert.assertEquals(dto.getId(), connection.getId());
        Assert.assertEquals(dto.getTimestamp(), connection.getStart());
        Assert.assertEquals(0L, connection.getDuration());
        Assert.assertNull(connection.getEnd());
        Assert.assertEquals(dto.getProtocol(), connection.getProtocol());
        Assert.assertEquals(dto.getSource(), connection.getSource());
        Assert.assertEquals(dto.getSourcePort(), connection.getSourcePort());
        Assert.assertEquals(dto.getDestination(), connection.getDestination());
        Assert.assertEquals(dto.getDestinationPort(), connection.getDestinationPort());
        Assert.assertEquals(dto.getUsername(), connection.getUsername());
        Assert.assertEquals(dto.getUid(), connection.getUserID());
        Assert.assertNotNull(connection.getProgram());
    }


}
