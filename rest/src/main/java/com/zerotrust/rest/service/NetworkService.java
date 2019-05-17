package com.zerotrust.rest.service;

import com.zerotrust.model.entity.Network;
import com.zerotrust.rest.dto.NetworkDTO;
import com.zerotrust.rest.exception.InvalidNetworkException;
import com.zerotrust.rest.exception.InvalidNetworkMaskException;

import java.util.Optional;

public interface NetworkService {
    Optional<Network> add(NetworkDTO dto) throws InvalidNetworkException, InvalidNetworkMaskException;

}
