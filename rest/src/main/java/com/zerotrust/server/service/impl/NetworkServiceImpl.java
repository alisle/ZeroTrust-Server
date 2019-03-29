package com.zerotrust.server.service.impl;

import com.zerotrust.server.model.Network;
import com.zerotrust.server.repository.NetworkRepository;
import com.zerotrust.server.service.NetworkService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NetworkServiceImpl implements NetworkService {
    private final NetworkRepository repository;
    private List<Network> networks;
    public NetworkServiceImpl(NetworkRepository repository) {
        this.repository = repository;
        this.networks = repository.findAll();
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
}
