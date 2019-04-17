package com.zerotrust.rest.service.impl;

import com.zerotrust.rest.events.NewCloseConnection;
import com.zerotrust.rest.events.NewOpenConnection;
import com.zerotrust.rest.exception.InvalidIPAddress;
import com.zerotrust.rest.mapper.ConnectionMapper;
import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.Network;
import com.zerotrust.rest.model.dto.ConnectionCloseDTO;
import com.zerotrust.rest.model.dto.ConnectionOpenDTO;
import com.zerotrust.rest.repository.ConnectionRepository;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.ConnectionService;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final AgentService agentService;
    private final ConnectionMapper connectionMapper;
    private final ConnectionRepository connectionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NetworkService networkService;

    public ConnectionServiceImpl(AgentService agentService, ConnectionMapper connectionMapper, ConnectionRepository connectionRepository, ApplicationEventPublisher applicationEventPublisher, NetworkService networkService) {
        this.agentService = agentService;
        this.connectionMapper = connectionMapper;
        this.connectionRepository = connectionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.networkService = networkService;
    }

    @Override
    public Optional<Connection> open(ConnectionOpenDTO dto) {
        try {

            Connection connection = connectionMapper.OpenDTOtoConnection(dto);
            if(connection.getId() == null) {
                return Optional.empty();
            }

            Network sourceNetwork = networkService.findMostRestrictiveNetwork(connection.getSource());
            Network destinationNetwork = networkService.findMostRestrictiveNetwork(connection.getDestination());

            connection.setSourceNetwork(sourceNetwork);
            connection.setDestinationNetwork(destinationNetwork);

            log.debug("saving connection:" + connection);
            agentService.seen(connection.getAgent());

            connection = connectionRepository.save(connection);

            if(connection != null) {
                applicationEventPublisher.publishEvent(new NewOpenConnection(this, connection));
            }

            connection = connectionRepository.save(connection);


            return Optional.ofNullable(connection);
        } catch (InvalidIPAddress exception ) {
            log.error("unable to create connection, ip address are invalid!");
            return Optional.empty();
        }

    }

    @Override
    public Optional<Connection> close(UUID id, Optional<Instant> timestamp) {
        Optional<Connection> option = connectionRepository.findById(id);
        if(!option.isPresent()) {
            // We don't seem to have the connection in our DB.
            return Optional.empty();
        }

        Connection connection = option.get();
        agentService.seen(connection.getAgent());


        connection.setEnd(timestamp.orElse(Instant.now()));
        connection.setDuration(connection.getEnd().toEpochMilli() - connection.getStart().toEpochMilli());

        connection = connectionRepository.save(connection);
        applicationEventPublisher.publishEvent(new NewCloseConnection(this, connection));

        return Optional.of(connectionRepository.save(connection));
    }

    @Override
    public Optional<Connection> close(ConnectionCloseDTO dto) {
        if(dto.getId() == null) {
            //  We don't know what started this so we'll ignore it.
            return Optional.empty();
        }
        return this.close(dto.getId(), Optional.of(dto.getTimestamp()));
    }

    @Override
    public Optional<Connection> get(UUID id) {
        return connectionRepository.findById(id);
    }

    @Override
    public List<Connection> findConnectionHash(long connectionHash) {
        return connectionRepository.findByConnectionHash(connectionHash);
    }

    @Override
    public List<Connection> findAliveConnections(UUID agent) {
        return connectionRepository.findByAliveIsTrueAndAgentId(agent);
    }

    @Override
    public List<Connection> aliveConnections(UUID uuid) {
        return findAliveConnections(uuid);
    }

    @Override
    public void validateConnections(UUID id, Set<Long> connections) {
        List<Connection> dbConnections = findAliveConnections(id);
        List<Connection> closedConnections = dbConnections.stream().
                filter(connection ->
                        !connections.contains(connection.getConnectionHash()))
                .collect(Collectors.toList());

        closedConnections.stream().forEach(connection -> {
            close(connection.getId(), Optional.empty());
        });
    }


}