package com.zerotrust.rest.service;

import com.google.common.net.InetAddresses;
import com.zerotrust.rest.model.IPAddress;
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
