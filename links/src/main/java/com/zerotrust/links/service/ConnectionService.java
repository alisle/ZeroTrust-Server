package com.zerotrust.links.service;

import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.model.Connection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface ConnectionService {
    Optional<Connection> open(ConnectionOpenDTO dto);
    Optional<Connection> close(ConnectionCloseDTO dto);
    Optional<Connection> close(UUID id, Optional<Instant> timestamp);
    List<Connection> findAliveConnections(UUID agent);
    Optional<Connection> get(UUID id);

    void validateConnections(UUID id, Set<Long> connections);
}
