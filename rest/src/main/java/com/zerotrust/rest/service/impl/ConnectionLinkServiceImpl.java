package com.zerotrust.rest.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zerotrust.rest.model.ConnectionLink;
import com.zerotrust.rest.repository.ConnectionLinkRepository;
import com.zerotrust.rest.service.ConnectionLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionLinkServiceImpl implements ConnectionLinkService  {
    private final ConnectionLinkRepository repository;

    @Value("${connection-link-service.use-cache}")
    private boolean useCache;

    private final Cache<Long, ConnectionLink> linkCache;

    public ConnectionLinkServiceImpl(ConnectionLinkRepository repository) {
        this.repository = repository;
        this.linkCache = CacheBuilder.newBuilder().maximumSize(2048).build();
    }

    @Override
    public List<ConnectionLink> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ConnectionLink> get(UUID id) {
        return repository.findById(id);
    }


    @Override
    public Page<ConnectionLink> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ConnectionLink> getByAgentConnections(Pageable pageable, UUID agent) {
        return repository.findAllByAgentId(pageable, agent);
    }

    @Override
    public Page<ConnectionLink> getLinksBetweenSourceAgentAndDestinationAgent(Pageable pageable, UUID source, UUID destination) {
        return repository.findAllBySourceAgentIdAndDestinationAgentId(pageable, source, destination);
    }

    @Override
    public Page<ConnectionLink> getLinksBetweenSourceAgentAndDestinationIP(Pageable pageable, UUID source, String address) {
        return repository.findAllBySourceAgentIdAndDestinationString(pageable, source, address);
    }

    @Override
    public Page<ConnectionLink> getLinksbetweenSourceIPAndDestinationAgent(Pageable pageable, String source, UUID destination) {
        return repository.findAllBySourceStringAndDestinationAgentId(pageable, source, destination);
    }

    @Override
    public Page<ConnectionLink> getActiveLinksFromSourceAgent(Pageable pageable, UUID source) {
        return repository.findAllBySourceAgentIdAndAliveTrue(pageable, source);
    }

    @Override
    public Page<ConnectionLink> getActiveLinksFromDestinationAgent(Pageable pageable, UUID destination) {
        return repository.findAllByDestinationAgentIdAndAliveTrue(pageable, destination);
    }

    @Override
    public Page<ConnectionLink> getAllActiveLinks(Pageable pageable) {
        return repository.findAllByAliveIsTrue(pageable);
    }

    @Override
    public long getTotalLinks() {
        return repository.countByAliveIsTrueOrAliveIsFalse();
    }

    @Override
    public long getTotalActiveLinks() {
        return repository.countByAliveIsTrue();
    }
}
