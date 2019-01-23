package com.notrust.server.service;


import com.google.common.net.InetAddresses;
import com.notrust.server.CreationUtils;
import com.notrust.server.ServerApplication;
import com.notrust.server.mapper.IPMapper;
import com.notrust.server.model.Agent;
import com.notrust.server.model.Connection;
import com.notrust.server.model.IPAddress;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import com.notrust.server.repository.AgentRepository;
import com.notrust.server.repository.ConnectionLinkRepository;
import com.notrust.server.repository.ConnectionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ConnectionLinkServiceImplTest {
    @Autowired
    AgentService agentService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    com.notrust.server.service.ConnectionLinkService linkService;

    @Autowired
    IPMapper ipMapper;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    ConnectionLinkRepository connectionLinkRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMismatchedIPs() throws Exception {
        IPAddress sourceAddress = ipMapper.convertString("192.168.0.1");
        Agent sourceAgent = agentService.online(UUID.randomUUID(), "SOURCE_AGENT");
        agentService.updateIPs(sourceAgent, new IPAddress[] { new IPAddress(0, "192.168.100.100", IPAddress.Version.V4 )});

        IPAddress destinationAddress = ipMapper.convertString("192.168.0.2");
        Agent destinationAgent = agentService.online(UUID.randomUUID(), "DESTINATION_AGENT");
        agentService.updateIPs(destinationAgent, new IPAddress[] { new IPAddress( 1, "192.168.100.1", IPAddress.Version.V4) });

        ConnectionOpenDTO sourceConnectionDTO = CreationUtils.ConnectionNewDTO();
        sourceConnectionDTO.setAgent(sourceAgent.getId());
        sourceConnectionDTO.setSource(sourceAddress.getAddressString());
        sourceConnectionDTO.setDestination(destinationAddress.getAddressString());
        sourceConnectionDTO.setHash(0L);
        connectionService.open(sourceConnectionDTO);


        ConnectionOpenDTO destinationConnectionDTO = CreationUtils.ConnectionNewDTO();
        destinationConnectionDTO.setAgent(destinationAgent.getId());
        destinationConnectionDTO.setSource(sourceAddress.getAddressString());
        destinationConnectionDTO.setDestination(destinationAddress.getAddressString());
        destinationConnectionDTO.setHash(0L);
        connectionService.open(destinationConnectionDTO);

        Assert.assertEquals(0, linkService.findAll().size());

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();
        agentRepository.deleteAll();
    }
    @Test
    public void testNewLinkSourceFirst() throws Exception {
        IPAddress sourceAddress = ipMapper.convertString("192.168.0.1");
        Agent sourceAgent = agentService.online(UUID.randomUUID(), "SOURCE_AGENT");
        agentService.updateIPs(sourceAgent, new IPAddress[] { sourceAddress });

        IPAddress destinationAddress = ipMapper.convertString("192.168.0.2");
        Agent destinationAgent = agentService.online(UUID.randomUUID(), "DESTINATION_AGENT");
        agentService.updateIPs(destinationAgent, new IPAddress[] { destinationAddress });

        ConnectionOpenDTO sourceConnectionDTO = CreationUtils.ConnectionNewDTO();
        sourceConnectionDTO.setAgent(sourceAgent.getId());
        sourceConnectionDTO.setSource(sourceAddress.getAddressString());
        sourceConnectionDTO.setDestination(destinationAddress.getAddressString());
        sourceConnectionDTO.setHash(0L);
        connectionService.open(sourceConnectionDTO);


        ConnectionOpenDTO destinationConnectionDTO = CreationUtils.ConnectionNewDTO();
        destinationConnectionDTO.setAgent(destinationAgent.getId());
        destinationConnectionDTO.setSource(sourceAddress.getAddressString());
        destinationConnectionDTO.setDestination(destinationAddress.getAddressString());
        destinationConnectionDTO.setHash(0L);
        connectionService.open(destinationConnectionDTO);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(0L, linkService.findAll().get(0).getConnectionHash());
        Assert.assertEquals(destinationAgent.getId(), linkService.findAll().get(0).getDestinationAgent().getId());
        Assert.assertEquals(sourceAgent.getId(), linkService.findAll().get(0).getSourceAgent().getId());

        Assert.assertEquals(destinationConnectionDTO.getId(), linkService.findAll().get(0).getDestinationConnection().getId());
        Assert.assertEquals(sourceConnectionDTO.getId(), linkService.findAll().get(0).getSourceConnection().getId());

        Assert.assertEquals(sourceConnectionDTO.getTimestamp(), linkService.findAll().get(0).getTimestamp());

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();
        agentRepository.deleteAll();
    }

    @Test
    public void testNewLinkDestinationFirst() throws Exception {
        IPAddress sourceAddress = ipMapper.convertString("192.168.1.1");
        Agent sourceAgent = agentService.online(UUID.randomUUID(), "SOURCE_AGENT");
        agentService.updateIPs(sourceAgent, new IPAddress[] { sourceAddress });

        IPAddress destinationAddress = ipMapper.convertString("192.168.1.2");
        Agent destinationAgent = agentService.online(UUID.randomUUID(), "DESTINATION_AGENT");
        agentService.updateIPs(destinationAgent, new IPAddress[] { destinationAddress });

        ConnectionOpenDTO destinationConnectionDTO = CreationUtils.ConnectionNewDTO();
        destinationConnectionDTO.setAgent(destinationAgent.getId());
        destinationConnectionDTO.setSource(sourceAddress.getAddressString());
        destinationConnectionDTO.setDestination(destinationAddress.getAddressString());
        destinationConnectionDTO.setHash(1L);
        connectionService.open(destinationConnectionDTO);



        ConnectionOpenDTO sourceConnectionDTO = CreationUtils.ConnectionNewDTO();
        sourceConnectionDTO.setAgent(sourceAgent.getId());
        sourceConnectionDTO.setSource(sourceAddress.getAddressString());
        sourceConnectionDTO.setDestination(destinationAddress.getAddressString());
        sourceConnectionDTO.setHash(1L);
        connectionService.open(sourceConnectionDTO);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(1L, linkService.findAll().get(0).getConnectionHash());
        Assert.assertEquals(destinationAgent.getId(), linkService.findAll().get(0).getDestinationAgent().getId());
        Assert.assertEquals(sourceAgent.getId(), linkService.findAll().get(0).getSourceAgent().getId());

        Assert.assertEquals(destinationConnectionDTO.getId(), linkService.findAll().get(0).getDestinationConnection().getId());
        Assert.assertEquals(sourceConnectionDTO.getId(), linkService.findAll().get(0).getSourceConnection().getId());

        Assert.assertEquals(sourceConnectionDTO.getTimestamp(), linkService.findAll().get(0).getTimestamp());

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();
        agentRepository.deleteAll();
    }

}
