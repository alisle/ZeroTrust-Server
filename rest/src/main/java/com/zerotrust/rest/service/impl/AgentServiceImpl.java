package com.zerotrust.rest.service.impl;

import com.zerotrust.rest.events.NewCloseConnection;
import com.zerotrust.rest.events.NewOpenConnection;
import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.model.*;
import com.zerotrust.rest.repository.AgentRepository;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class AgentServiceImpl implements AgentService {
    private final NetworkService networkService;
    private final AgentRepository agentRepository;

    public AgentServiceImpl(NetworkService networkService, AgentRepository agentRepository) {
        this.networkService = networkService;
        this.agentRepository = agentRepository;
    }

    @Override
    public Optional<Agent> get(UUID uuid) {
        return agentRepository.findById(uuid);
    }

    @Override
    public Agent unknown(UUID uuid, String name) {
        Agent agent = new Agent();
        agent.setId(uuid);
        agent.setName(name);
        agent.setKnown(false);
        agent.setFirstSeen(Instant.now());
        agent.setAlive(false);

        return agentRepository.save(agent);
    }

    @Override
    public Agent online(UUID uuid, String name) {
        Agent agent = agentRepository.findById(uuid).orElseGet(() -> unknown(uuid, name));
        agent.setName(name);
        agent.setKnown(true);
        agent.setAlive(true);
        agent.setLastSeen(Instant.now());

        return agentRepository.save(agent);
    }

    @Override
    public void offline(UUID uuid) throws AgentNotFoundException {
        Agent agent = agentRepository.findById(uuid).orElseThrow(() -> new AgentNotFoundException());
        agent.setAlive(false);
        agentRepository.save(agent);
    }


    @Override
    public Agent unknown(UUID uuid) {
        return unknown(uuid, null);
    }

    @Override
    public void seen(Agent agent) {
        if(agent.getFirstSeen() == null) {
            agent.setLastSeen(Instant.now());
        }

        agent.setLastSeen(Instant.now());
        agent.setAlive(true);

        agentRepository.save(agent);
    }

    @Override
    public void seen(UUID uuid) {
        Agent agent = agentRepository.findById(uuid).orElseGet(() -> unknown(uuid));
        seen(agent);
    }

    @Override
    public void updateIPs(UUID uuid, IPAddress[] ips) throws AgentNotFoundException {
        Agent agent = agentRepository.findById(uuid).orElseThrow(() -> new AgentNotFoundException());
        updateIPs(agent, ips);
    }

    @Override
    public void updateIPs(Agent agent, IPAddress[] ips) {
        agent.getAddresses().clear();
        agent.getAddresses().addAll(Arrays.asList(ips));

        agent.getNetworks().clear();
        List<Network> networks = new ArrayList<>();
        Arrays.stream(ips).forEach(ip -> {
            networks.addAll(networkService.findNetworks(ip.getAddress(), false));
        });
        agent.getNetworks().addAll(networks);

        seen(agent);
    }

    @Override
    public List<UserCount> sourceUsersCount(UUID uuid) throws AgentNotFoundException{
        try {
            return agentRepository.countSourceUsername(uuid);
        } catch (Exception ex) {
            throw new AgentNotFoundException();
        }
    }

    @Override
    public List<UserCount> destinationUsersCount(UUID uuid) throws AgentNotFoundException {
        try {
            return agentRepository.countDestinationUsername(uuid);
        } catch (Exception ex) {
            throw new AgentNotFoundException();
        }
    }

    @Override
    public List<UserCount> allUsersCount(UUID uuid) throws AgentNotFoundException {
        try {
            return agentRepository.countUsername(uuid);
        } catch (Exception ex) {
            throw new AgentNotFoundException();
        }

    }

    @Override
    public List<ProcessCount> sourceProcessCount(UUID uuid) throws AgentNotFoundException {
        try {
            return agentRepository.countSourceProcess(uuid);
        } catch (Exception ex) {
            throw new AgentNotFoundException();
        }
    }

    @Override
    public List<ProcessCount> destinationProcessCount(UUID uuid) throws AgentNotFoundException {
        try {
            return agentRepository.countDestinationProcess(uuid);
        } catch (Exception ex) {
            throw new AgentNotFoundException();
        }
    }

    @Override
    public List<AgentCount> countIncomingConnections() {
        return agentRepository.countIncomingConnections();
    }

    @Override
    public List<AgentCount> countOutgoingConnections() {
        return agentRepository.countOutgoingConnections();
    }

    @org.springframework.context.event.EventListener
    public void onApplicationEvent(NewOpenConnection newOpenConnection) {
        seen(newOpenConnection.getConnection().getAgent());
    }

    @EventListener
    public void onApplicationEvent(NewCloseConnection newCloseConnection) {
        seen(newCloseConnection.getConnection().getAgent());
    }

}


