package com.zerotrust.server.service;

import com.zerotrust.server.model.AgentCount;
import com.zerotrust.server.model.Network;

import java.util.List;
import java.util.UUID;

public interface NetworkService {
    Network findMostRestrictiveNetwork(int address);
    List<Network> findNetworks(int address, boolean includeInternet);

    List<AgentCount> countActiveDestinationConnections(UUID network);
    List<AgentCount> countActiveSourceConnections(UUID networks);

}
