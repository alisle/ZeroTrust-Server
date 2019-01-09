package com.notrust.server.service.impl;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.repository.AgentRepository;
import com.notrust.server.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
}
