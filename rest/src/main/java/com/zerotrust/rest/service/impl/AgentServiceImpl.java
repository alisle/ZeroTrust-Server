package com.zerotrust.rest.service.impl;

import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.model.*;
import com.zerotrust.rest.repository.AgentRepository;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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



    @Override
    public Page<Agent> getPage(Pageable pageable) {
        return agentRepository.findAll(pageable);
    }

    @Override
    public long totalAliveAgents() {
        return agentRepository.countByAliveIsTrue();
    }

    @Override
    public long totalAgents() {
        return agentRepository.countByAliveIsTrueOrAliveIsFalse();
    }
}


