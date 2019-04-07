package com.zerotrust.rest.service;

import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.dto.ConnectionCloseDTO;
import com.zerotrust.rest.model.dto.ConnectionOpenDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ConnectionService {
    Optional<Connection> open(ConnectionOpenDTO dto);
    Optional<Connection> close(ConnectionCloseDTO dto);
    Optional<Connection> close(UUID id, Optional<Instant> timestamp);

    Optional<Connection> get(UUID id);

    List<Connection> findAliveConnections(UUID agent);
    List<Connection> findConnectionHash(long connectionHash);
    List<Connection> aliveConnections(UUID uuid);
    void validateConnections(UUID id, Set<Long> connections);
}
