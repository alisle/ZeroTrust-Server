package com.notrust.server.service.impl;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.model.IPAddress;
import com.notrust.server.model.ProcessCount;
import com.notrust.server.model.UserCount;
import com.notrust.server.repository.AgentRepository;
import com.notrust.server.repository.ConnectionLinkRepository;
import com.notrust.server.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
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
}


