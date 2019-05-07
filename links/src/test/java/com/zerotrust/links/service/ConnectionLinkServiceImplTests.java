package com.zerotrust.links.service;


import com.zerotrust.links.CreationUtils;
import com.zerotrust.links.LinksServerApplication;
import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.links.mapper.IPMapper;
import com.zerotrust.links.model.Agent;
import com.zerotrust.links.model.ConnectionLink;
import com.zerotrust.links.model.IPAddress;
import com.zerotrust.links.repository.AgentRepository;
import com.zerotrust.links.repository.ConnectionLinkRepository;
import com.zerotrust.links.repository.ConnectionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinksServerApplication.class)
@ActiveProfiles("test")
public class ConnectionLinkServiceImplTests {

    @Autowired
    AgentService agentService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    ConnectionLinkService linkService;

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
    public void testSourceNoDestination() throws Exception {
        IPAddress sourceAddress = ipMapper.convertString("192.168.0.1");
        Agent sourceAgent = agentService.online(UUID.randomUUID(), "SOURCE_AGENT");
        agentService.updateIPs(sourceAgent, new IPAddress[] { sourceAddress });

        ConnectionOpenDTO sourceConnectionDTO = CreationUtils.ConnectionNewDTO();
        sourceConnectionDTO.setAgent(sourceAgent.getId());
        sourceConnectionDTO.setSource(sourceAddress.getAddressString());
        sourceConnectionDTO.setDestination("192.168.0.200");
        sourceConnectionDTO.setHash(0L);
        connectionService.open(sourceConnectionDTO);


        ConnectionLink link = linkService.findAll().get(0);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(0L, link.getConnectionHash());
        Assert.assertNull(link.getDestinationAgent());
        Assert.assertEquals(sourceAgent.getId(), link.getSourceAgent().getId());

        Assert.assertNull(link.getDestinationConnection());

        Assert.assertEquals("192.168.0.200", link.getDestinationString());
        Assert.assertEquals(sourceConnectionDTO.getDestinationPort(), link.getDestinationPort());

        Assert.assertEquals("192.168.0.1", link.getSourceString());
        Assert.assertEquals(sourceConnectionDTO.getSourcePort(), link.getSourcePort());

        Assert.assertEquals(sourceConnectionDTO.getId(), link.getSourceConnection().getId());

        Assert.assertEquals(sourceConnectionDTO.getTimestamp(), link.getTimestamp());
        Assert.assertTrue(link.isAlive());


        linkService.get(link.getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(sourceAgent.getId());
    }


    @Test
    public void testDestinationNoSource() throws Exception {
        IPAddress address = ipMapper.convertString("192.168.0.1");
        Agent agent = agentService.online(UUID.randomUUID(), "SOURCE_AGENT");
        agentService.updateIPs(agent, new IPAddress[] { address });

        ConnectionOpenDTO connectionNewDTO = CreationUtils.ConnectionNewDTO();
        connectionNewDTO.setAgent(agent.getId());
        connectionNewDTO.setSource("192.168.0.200");
        connectionNewDTO.setDestination(address.getAddressString());
        connectionNewDTO.setHash(0L);
        connectionService.open(connectionNewDTO);


        ConnectionLink link = linkService.findAll().get(0);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(0L, link.getConnectionHash());
        Assert.assertNull(link.getSourceAgent());
        Assert.assertEquals(agent.getId(), link.getDestinationAgent().getId());

        Assert.assertNull(link.getSourceConnection());
        Assert.assertEquals(connectionNewDTO.getId(), link.getDestinationConnection().getId());


        Assert.assertEquals(address.getAddressString(), link.getDestinationString());
        Assert.assertEquals(connectionNewDTO.getDestinationPort(), link.getDestinationPort());

        Assert.assertEquals("192.168.0.200", link.getSourceString());
        Assert.assertEquals(connectionNewDTO.getSourcePort(), link.getSourcePort());


        Assert.assertEquals(connectionNewDTO.getTimestamp(), link.getTimestamp());
        Assert.assertTrue(link.isAlive());

        linkService.get(link.getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(agent.getId());
    }

    @Test
    public void testCloseSourceAgent() throws Exception {
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

        ConnectionCloseDTO closeDTO = CreationUtils.ConnectionCloseDTO();
        closeDTO.setAgent(sourceAgent.getId());
        closeDTO.setSource(sourceAddress.getAddressString());
        closeDTO.setHash(0L);
        closeDTO.setId(sourceConnectionDTO.getId());

        connectionService.close(closeDTO);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(0L, linkService.findAll().get(0).getConnectionHash());
        Assert.assertFalse(linkService.findAll().get(0).isAlive());

        linkService.get(linkService.findAll().get(0).getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));


        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(sourceAgent.getId());
        agentRepository.deleteById(destinationAgent.getId());
    }
    @Test
    public void testCloseDestinationAgent() throws Exception {
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

        ConnectionCloseDTO  closeDTO = CreationUtils.ConnectionCloseDTO();
        closeDTO.setAgent(destinationAgent.getId());
        closeDTO.setSource(destinationAddress.getAddressString());
        closeDTO.setId(sourceConnectionDTO.getId());

        connectionService.close(closeDTO);

        Assert.assertEquals(1, linkService.findAll().size());
        Assert.assertEquals(0L, linkService.findAll().get(0).getConnectionHash());
        Assert.assertFalse(linkService.findAll().get(0).isAlive());

        linkService.get(linkService.findAll().get(0).getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(sourceAgent.getId());
        agentRepository.deleteById(destinationAgent.getId());
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

        agentRepository.deleteById(sourceAgent.getId());
        agentRepository.deleteById(destinationAgent.getId());
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
        Assert.assertTrue(linkService.findAll().get(0).isAlive());

        linkService.get(linkService.findAll().get(0).getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(sourceAgent.getId());
        agentRepository.deleteById(destinationAgent.getId());
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
        Assert.assertTrue(linkService.findAll().get(0).isAlive());

        linkService.get(linkService.findAll().get(0).getId()).orElseThrow(() -> new RuntimeException("Oh Dear"));

        connectionLinkRepository.deleteAll();
        connectionRepository.deleteAll();

        agentRepository.deleteById(sourceAgent.getId());
        agentRepository.deleteById(destinationAgent.getId());
    }

}
