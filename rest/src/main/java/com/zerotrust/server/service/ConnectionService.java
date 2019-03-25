package com.zerotrust.server.service;

import com.zerotrust.server.model.Connection;
import com.zerotrust.server.model.dto.ConnectionCloseDTO;
import com.zerotrust.server.model.dto.ConnectionOpenDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionService {
    Optional<Connection> open(ConnectionOpenDTO dto);
    Optional<Connection> close(ConnectionCloseDTO dto);
    Optional<Connection> get(UUID id);

    List<Connection> findAliveConnections(UUID agent);
    List<Connection> findConnectionHash(long connectionHash);
}
