package com.notrust.server.service.impl;

import com.notrust.server.mapper.ConnectionMapper;
import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionCloseDTO;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import com.notrust.server.repository.ConnectionRepository;
import com.notrust.server.service.AgentService;
import com.notrust.server.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionMapper connectionMapper;
    private final ConnectionRepository connectionRepository;
    private final AgentService agentService;

    public ConnectionServiceImpl(ConnectionMapper connectionMapper, ConnectionRepository connectionRepository, AgentService agentService) {
        this.connectionMapper = connectionMapper;
        this.connectionRepository = connectionRepository;
        this.agentService = agentService;
    }

    @Override
    public Optional<Connection> open(ConnectionOpenDTO dto) {
        Connection connection = connectionMapper.OpenDTOtoConnection(dto);
        if(connection.getId() == null) {
            return Optional.empty();
        }

        agentService.seen(connection.getAgent());

        connection = connectionRepository.save(connection);
        return Optional.ofNullable(connection);
    }

    @Override
    public Optional<Connection> close(ConnectionCloseDTO dto) {
        if(dto.getId() == null) {
            //  We don't know what started this so we'll ignore it.
            return Optional.empty();
        }


        Optional<Connection> option = connectionRepository.findById(dto.getId());
        if(!option.isPresent()) {
            // We donn't seem to have the connection in our DB.
            return Optional.empty();
        }


        Connection connection = option.get();
        agentService.seen(connection.getAgent());

        connection.setEnd(dto.getTimestamp());
        connection.setDuration(connection.getEnd().toEpochMilli() - connection.getStart().toEpochMilli());
        connection = connectionRepository.save(connection);

        return Optional.ofNullable(connection);
    }

    @Override
    public Optional<Connection> get(UUID id) {
        return connectionRepository.findById(id);
    }
}
