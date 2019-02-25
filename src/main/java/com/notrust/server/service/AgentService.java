package com.notrust.server.service;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.model.IPAddress;
import com.notrust.server.model.ProcessCount;
import com.notrust.server.model.UserCount;

import java.util.List;
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

    List<UserCount> sourceUsersCount(UUID uuid) throws AgentNotFoundException;
    List<UserCount> destinationUsersCount(UUID uuid) throws AgentNotFoundException;
    List<UserCount> allUsersCount(UUID uuid) throws AgentNotFoundException;


    List<ProcessCount> sourceProcessCount(UUID uuid) throws AgentNotFoundException;
    List<ProcessCount> destinationProcessCount(UUID uuid) throws AgentNotFoundException;


}
