package com.zerotrust.links.service.impl;

import com.zerotrust.links.event.NewCloseConnection;
import com.zerotrust.links.event.NewOpenConnection;
import com.zerotrust.links.exception.AgentNotFoundException;
import com.zerotrust.model.Agent;
import com.zerotrust.model.IPAddress;
import com.zerotrust.model.Network;
import com.zerotrust.links.repository.AgentRepository;
import com.zerotrust.links.service.AgentService;
import com.zerotrust.links.service.NetworkService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final NetworkService networkService;

    public AgentServiceImpl(AgentRepository agentRepository, NetworkService networkService) {
        this.agentRepository = agentRepository;
        this.networkService = networkService;
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
    public Agent unknown(UUID uuid) {
        return unknown(uuid, null);
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
    public Optional<Agent> get(UUID uuid) {
        return agentRepository.findById(uuid);
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
