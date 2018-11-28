package com.notrust.server.service;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;

import java.util.Optional;
import java.util.UUID;

public interface AgentService {
    void seen(UUID uuid);

    Agent unknown(UUID uuid, String name);
    Agent unknown(UUID uuid);

    Agent online(UUID uuid, String name);
    void offline(UUID uuid) throws AgentNotFoundException;

    Optional<Agent> get(UUID uuid);
}
