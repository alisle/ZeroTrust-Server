package com.notrust.server.service;

import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionCloseDTO;
import com.notrust.server.model.dto.ConnectionOpenDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionService {
    Optional<Connection> open(ConnectionOpenDTO dto);
    Optional<Connection> close(ConnectionCloseDTO dto);
    Optional<Connection> get(UUID id);

    List<Connection> findConnectionHash(long connectionHash);
}
