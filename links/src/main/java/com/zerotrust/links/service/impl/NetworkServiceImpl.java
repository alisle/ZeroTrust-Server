package com.zerotrust.links.service.impl;

import com.zerotrust.model.entity.Network;
import com.zerotrust.links.repository.NetworkRepository;
import com.zerotrust.links.service.NetworkService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NetworkServiceImpl implements NetworkService {
    private final NetworkRepository repository;

    public NetworkServiceImpl(NetworkRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Network> findNetworks(int address, boolean includeInternet) {
        List<Network> networks = repository.findAll();

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

}
