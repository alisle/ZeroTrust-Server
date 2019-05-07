package com.zerotrust.rest.service;

import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.Network;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NetworkService {

    List<AgentCount> countActiveDestinationConnections(UUID network);
    List<AgentCount> countActiveSourceConnections(UUID networks);

    Page<Network> getPage(Pageable pageable);
}
