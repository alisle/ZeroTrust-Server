package com.notrust.server.service;

import com.notrust.server.exception.ConnectionLinkNotFoundException;
import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionLinkService {
    void open(Connection connection);
    void close(Connection connection);
    List<ConnectionLink> findAll();

    Optional<ConnectionLink> get(UUID id);
}
