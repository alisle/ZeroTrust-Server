package com.zerotrust.server.service;

import com.zerotrust.server.model.Network;

import java.util.List;

public interface NetworkService {
    List<Network> findNetworks(int address, boolean includeInternet);
}
