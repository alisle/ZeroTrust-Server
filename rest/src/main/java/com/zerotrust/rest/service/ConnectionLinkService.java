package com.zerotrust.rest.service;

import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.ConnectionLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionLinkService {
    List<ConnectionLink> findAll();
    Page<ConnectionLink> getPage(Pageable pageable);
    Optional<ConnectionLink> get(UUID id);

    Page<ConnectionLink> getByAgentConnections(Pageable pageable, UUID agent);
    Page<ConnectionLink> getLinksBetweenSourceAgentAndDestinationAgent(Pageable pageable, UUID source, UUID destination);
    Page<ConnectionLink> getLinksBetweenSourceAgentAndDestinationIP(Pageable pageable, UUID source, String address);
    Page<ConnectionLink> getLinksbetweenSourceIPAndDestinationAgent(Pageable pageable, String source, UUID destination);
    Page<ConnectionLink> getActiveLinksFromSourceAgent(Pageable pageable, UUID source);
    Page<ConnectionLink> getActiveLinksFromDestinationAgent(Pageable pageable, UUID destination);
    Page<ConnectionLink> getAllActiveLinks(Pageable pageable);
    long getTotalLinks();
    long getTotalActiveLinks();

}
