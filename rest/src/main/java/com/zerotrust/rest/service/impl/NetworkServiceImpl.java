package com.zerotrust.rest.service.impl;

import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.Network;
import com.zerotrust.rest.repository.NetworkRepository;
import com.zerotrust.rest.service.NetworkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NetworkServiceImpl implements NetworkService {
    private final NetworkRepository repository;
    public NetworkServiceImpl(NetworkRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AgentCount> countActiveDestinationConnections(UUID network) {
        return repository.activeDestinationConnections(network);
    }


    @Override
    public List<AgentCount> countActiveSourceConnections(UUID network) {
        return repository.activeSourceConnections(network);
    }

    @Override
    public Page<Network> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
