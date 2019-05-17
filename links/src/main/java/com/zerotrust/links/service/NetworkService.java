package com.zerotrust.links.service;

import com.zerotrust.model.entity.Network;

import java.util.List;

public interface NetworkService {
    Network findMostRestrictiveNetwork(int address);
    List<Network> findNetworks(int address, boolean includeInternet);

}
