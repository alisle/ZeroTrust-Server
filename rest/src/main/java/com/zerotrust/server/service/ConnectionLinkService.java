package com.zerotrust.server.service;

import com.zerotrust.server.model.Connection;
import com.zerotrust.server.model.ConnectionLink;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionLinkService {
    void open(Connection connection);
    void close(Connection connection);
    List<ConnectionLink> findAll();

    Optional<ConnectionLink> get(UUID id);
}
