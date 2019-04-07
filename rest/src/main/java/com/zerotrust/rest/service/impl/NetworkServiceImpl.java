package com.zerotrust.rest.service.impl;

import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.Network;
import com.zerotrust.rest.repository.NetworkRepository;
import com.zerotrust.rest.service.NetworkService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NetworkServiceImpl implements NetworkService {
    private final NetworkRepository repository;
    private List<Network> networks;
    public NetworkServiceImpl(NetworkRepository repository) {
        this.repository = repository;
        this.networks = repository.findAll();
    }

    public Network findMostRestrictiveNetwork(int address) {
        List<Network> list = findNetworks(address, true);
        Network best = list.get(0);
        for(int x = 0; x < list.size(); x++) {
            Network network = list.get(x);
            if(Integer.compareUnsigned(best.getMask(), list.get(x).getMask()) < 0) {
                best = network;
            }
        }
        return best;
    }

    @Override
    public List<Network> findNetworks(int address, boolean includeInternet) {
        return networks.stream().map(network -> {
            if(!includeInternet && network.getMask() == 0) {
                return null;
            }

            int networkAddress = network.getAddress() & network.getMask();
            int ipAddress = address & network.getMask();

            if(networkAddress == ipAddress) {
                return network;
            }

            return null;
        }).filter(network -> network != null).collect(Collectors.toList());
    }

    @Override
    public List<AgentCount> countActiveDestinationConnections(UUID network) {
        return repository.activeDestinationConnections(network);
    }


    @Override
    public List<AgentCount> countActiveSourceConnections(UUID network) {
        return repository.activeSourceConnections(network);
    }

}
