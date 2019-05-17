package com.zerotrust.links.service;

import com.zerotrust.model.entity.Connection;
import com.zerotrust.model.entity.ConnectionLink;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionLinkService {
    void open(Connection connection);
    void close(Connection connection);
    Optional<ConnectionLink> get(UUID id);
    List<ConnectionLink> findAll();

}
