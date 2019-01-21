package com.notrust.server.service;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.model.IPAddress;

import java.util.Optional;
import java.util.UUID;

public interface AgentService {
    void seen(UUID uuid);
    void seen(Agent agent);

    Agent unknown(UUID uuid, String name);
    Agent unknown(UUID uuid);

    Agent online(UUID uuid, String name);
    void offline(UUID uuid) throws AgentNotFoundException;

    Optional<Agent> get(UUID uuid);

    void updateIPs(UUID uuid, IPAddress[] ips) throws AgentNotFoundException;
    void updateIPs(Agent agent, IPAddress[] ips);
}
