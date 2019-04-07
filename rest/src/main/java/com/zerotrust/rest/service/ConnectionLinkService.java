package com.zerotrust.rest.service;

import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.ConnectionLink;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionLinkService {
    void open(Connection connection);
    void close(Connection connection);
    List<ConnectionLink> findAll();

    Optional<ConnectionLink> get(UUID id);
}
