package com.zerotrust.links.service.impl;

import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.links.event.NewCloseConnection;
import com.zerotrust.links.event.NewOpenConnection;
import com.zerotrust.links.exception.InvalidIPAddress;
import com.zerotrust.links.mapper.ConnectionMapper;
import com.zerotrust.links.model.Connection;
import com.zerotrust.links.model.Network;
import com.zerotrust.links.repository.ConnectionRepository;
import com.zerotrust.links.service.AgentService;
import com.zerotrust.links.service.ConnectionService;
import com.zerotrust.links.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {
    private final AgentService agentService;
    private final ConnectionRepository connectionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NetworkService networkService;
    private final ConnectionMapper connectionMapper;

    public ConnectionServiceImpl(AgentService agentService, ConnectionRepository connectionRepository, ApplicationEventPublisher applicationEventPublisher, NetworkService networkService, ConnectionMapper connectionMapper) {
        this.agentService = agentService;
        this.connectionRepository = connectionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.networkService = networkService;
        this.connectionMapper = connectionMapper;
    }

    @Override
    public void validateConnections(UUID id, Set<Long> connections) {
        List<Connection> dbConnections = connectionRepository.findByAliveIsTrueAndAgentId(id);
        List<Connection> closedConnections = dbConnections.stream().
                filter(connection ->
                        !connections.contains(connection.getConnectionHash()))
                .collect(Collectors.toList());

        closedConnections.stream().forEach(connection -> {
            close(connection.getId(), Optional.empty());
        });
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
    public List<Connection> findAliveConnections(UUID agent) {
        return connectionRepository.findByAliveIsTrueAndAgentId(agent);
    }

}
