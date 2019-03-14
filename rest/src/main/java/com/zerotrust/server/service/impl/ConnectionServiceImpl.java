package com.zerotrust.server.service.impl;

import com.zerotrust.server.events.NewCloseConnection;
import com.zerotrust.server.events.NewOpenConnection;
import com.zerotrust.server.exception.InvalidIPAddress;
import com.zerotrust.server.mapper.ConnectionMapper;
import com.zerotrust.server.model.Connection;
import com.zerotrust.server.model.dto.ConnectionCloseDTO;
import com.zerotrust.server.model.dto.ConnectionOpenDTO;
import com.zerotrust.server.repository.ConnectionRepository;
import com.zerotrust.server.service.AgentService;
import com.zerotrust.server.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionMapper connectionMapper;
    private final ConnectionRepository connectionRepository;
    private final AgentService agentService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ConnectionServiceImpl(ConnectionMapper connectionMapper, ConnectionRepository connectionRepository, AgentService agentService, ApplicationEventPublisher applicationEventPublisher) {
        this.connectionMapper = connectionMapper;
        this.connectionRepository = connectionRepository;
        this.agentService = agentService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Optional<Connection> open(ConnectionOpenDTO dto) {
        try {
            Connection connection = connectionMapper.OpenDTOtoConnection(dto);
            if(connection.getId() == null) {
                return Optional.empty();
            }

            agentService.seen(connection.getAgent());

            connection = connectionRepository.save(connection);

            if(connection != null) {
                applicationEventPublisher.publishEvent(new NewOpenConnection(this, connection));
            }

            return Optional.ofNullable(connection);
        } catch (InvalidIPAddress exception ) {
            log.error("unable to create connection, ip address are invalid!");
            return Optional.empty();
        }

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
        applicationEventPublisher.publishEvent(new NewCloseConnection(this, connection));

        return Optional.ofNullable(connection);
    }

    @Override
    public Optional<Connection> get(UUID id) {
        return connectionRepository.findById(id);
    }

    @Override
    public List<Connection> findConnectionHash(long connectionHash) {
        return connectionRepository.findByConnectionHash(connectionHash);
    }
}
