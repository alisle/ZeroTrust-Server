package com.zerotrust.links.service;

import com.zerotrust.links.exception.AgentNotFoundException;
import com.zerotrust.model.entity.Agent;
import com.zerotrust.model.entity.IPAddress;

import java.util.Optional;
import java.util.UUID;

public interface AgentService {
    void seen(UUID uuid);
    void seen(Agent agent);

    Agent unknown(UUID uuid, String name);
    Agent unknown(UUID uuid);

    Agent online(UUID uuid, String name);
    void offline(UUID uuid) throws AgentNotFoundException;

    void updateIPs(UUID uuid, IPAddress[] ips) throws AgentNotFoundException;
    void updateIPs(Agent agent, IPAddress[] ips);

    Optional<Agent> get(UUID uuid);
}
