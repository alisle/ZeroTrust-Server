package com.notrust.server.service;

import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConnectionLinkService {
    void open(Connection connection);
    void close(Connection connection);
    List<ConnectionLink> findAll();
}
