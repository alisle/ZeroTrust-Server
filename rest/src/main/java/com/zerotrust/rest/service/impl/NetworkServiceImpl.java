package com.zerotrust.rest.service.impl;

import com.zerotrust.model.entity.Network;
import com.zerotrust.rest.dto.NetworkDTO;
import com.zerotrust.rest.exception.InvalidNetworkException;
import com.zerotrust.rest.exception.InvalidNetworkMaskException;
import com.zerotrust.rest.mapper.NetworkMapper;
import com.zerotrust.rest.repository.NetworkRepository;
import com.zerotrust.rest.service.NetworkService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NetworkServiceImpl implements NetworkService {
    private final NetworkRepository repository;
    private final NetworkMapper mapper;

    public NetworkServiceImpl(NetworkRepository repository, NetworkMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Network> add(NetworkDTO dto) throws InvalidNetworkException, InvalidNetworkMaskException {
        Network network = mapper.convert(dto);
        network.setId(UUID.randomUUID());
        return Optional.ofNullable(repository.save(network));
    }
}
