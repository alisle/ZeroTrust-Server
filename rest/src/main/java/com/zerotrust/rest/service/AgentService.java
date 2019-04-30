package com.zerotrust.rest.service;

import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Agent> getPage(Pageable pageable);
    Optional<Agent> get(UUID uuid);

    void updateIPs(UUID uuid, IPAddress[] ips) throws AgentNotFoundException;
    void updateIPs(Agent agent, IPAddress[] ips);

    List<UserCount> sourceUsersCount(UUID uuid) throws AgentNotFoundException;
    List<UserCount> destinationUsersCount(UUID uuid) throws AgentNotFoundException;
    List<UserCount> allUsersCount(UUID uuid) throws AgentNotFoundException;


    List<ProcessCount> sourceProcessCount(UUID uuid) throws AgentNotFoundException;
    List<ProcessCount> destinationProcessCount(UUID uuid) throws AgentNotFoundException;

    List<AgentCount> countIncomingConnections();
    List<AgentCount> countOutgoingConnections();

    long totalAliveAgents();
    long totalAgents();
}
