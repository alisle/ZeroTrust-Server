package com.zerotrust.links.service;

import com.zerotrust.links.model.Network;

import java.util.List;
import java.util.UUID;

public interface NetworkService {
    Network findMostRestrictiveNetwork(int address);
    List<Network> findNetworks(int address, boolean includeInternet);

}
