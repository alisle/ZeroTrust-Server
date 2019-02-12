package com.notrust.server.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.notrust.server.events.NewOpenConnection;
import com.notrust.server.model.Agent;
import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import com.notrust.server.model.IPAddress;
import com.notrust.server.repository.ConnectionLinkRepository;
import com.notrust.server.service.AgentService;
import com.notrust.server.service.ConnectionLinkService;
import com.notrust.server.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionLinkServiceImpl implements ConnectionLinkService, ApplicationListener<NewOpenConnection> {
    private static final UUID unknownAgentID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final ConnectionLinkRepository repository;
    private final ConnectionService connectionService;
    private final AgentService agentService;

    private final Cache<Long, Connection> cache;
    private final Cache<Long, ConnectionLink> linkCache;

    public ConnectionLinkServiceImpl(ConnectionLinkRepository repository, ConnectionService connectionService, AgentService agentService) {
        this.repository = repository;
        this.connectionService = connectionService;
        this.agentService = agentService;
        this.cache = CacheBuilder.newBuilder().maximumSize(100).build();
        this.linkCache = CacheBuilder.newBuilder().maximumSize(2048).build();
    }


    private Optional<ConnectionLink> findLinks(Connection connection) {
        ConnectionLink potential = linkCache.getIfPresent(connection.getConnectionHash());
        if( potential != null) {
            log.debug("found matching link in cache");
            return Optional.of(potential);
        }

        log.debug("unable to find link in cache, looking in the DB");
        List<ConnectionLink> potentials = repository.findAllByConnectionHashAndSourceAgentIdOrDestinationAgentId(connection.getConnectionHash(), unknownAgentID, unknownAgentID);
        Optional<ConnectionLink> best = Optional.empty();
        Instant bestTime = Instant.MIN;
        for (ConnectionLink link : potentials) {
            boolean alive = (link.getSourceConnection() != null && link.getSourceConnection().isAlive())  | (link.getDestinationConnection() != null && link.getDestinationConnection().isAlive());
            if(alive) {
                Instant time = (link.getSourceConnection() != null) ? link.getSourceConnection().getStart() : link.getDestinationConnection().getStart();
                if(!best.isPresent()) {
                    best = Optional.of(link);
                    bestTime = time;
                } else {
                    if(bestTime.isBefore(time)) {
                        best = Optional.of(link);
                        bestTime = time;
                    }
                }
            }
        }

        return best;
    }


    private Optional<ConnectionLink> updateLink(ConnectionLink link, Connection connection) {
        Optional<Connection> source = Optional.empty();
        Optional<Connection> destination = Optional.empty();

        IPAddress sourceAddress = new IPAddress(connection.getSource(), connection.getSourceString(), IPAddress.Version.V4);
        IPAddress destinationAddress = new IPAddress(connection.getDestination(), connection.getDestinationString(), IPAddress.Version.V4);
        Set<IPAddress> addresses  = connection.getAgent().getAddresses();

        if(addresses.contains(sourceAddress)) {
            source = Optional.of(connection);
        }

        if(addresses.contains(destinationAddress)) {
            destination = Optional.of(connection);
        }

        source.ifPresent(con -> {
            link.setTimestamp(con.getStart());
            link.setSourceConnection(con);
            link.setSourceAgent(con.getAgent());
        });

        destination.ifPresent(con -> {
            link.setDestinationConnection(con);
            link.setDestinationAgent(con.getAgent());
        });


        return Optional.of(link);
    }


    private Optional<ConnectionLink> createLink(Connection connection) {
        Optional<Connection> source = Optional.empty();
        Optional<Connection> destination = Optional.empty();

        final ConnectionLink link = new ConnectionLink();
        link.setTimestamp(connection.getStart());
        link.setId(UUID.randomUUID());
        link.setConnectionHash(connection.getConnectionHash());
        link.setAlive(true);

        IPAddress sourceAddress = new IPAddress(connection.getSource(), connection.getSourceString(), IPAddress.Version.V4);
        IPAddress destinationAddress = new IPAddress(connection.getDestination(), connection.getDestinationString(), IPAddress.Version.V4);
        Set<IPAddress> addresses  = connection.getAgent().getAddresses();


        if(addresses.contains(sourceAddress)) {
            source = Optional.of(connection);
        }

        if(addresses.contains(destinationAddress)) {
            destination = Optional.of(connection);
        }

        if(!destination.isPresent() && !source.isPresent()) {
            return Optional.empty();
        }

        source.ifPresentOrElse(con -> {
            link.setSourceConnection(con);
            link.setSourceAgent(con.getAgent());
            link.setSourceProcessName(con.getProcessName());
            link.setTimestamp(con.getStart());
        }, () -> agentService.get(unknownAgentID).ifPresent(agent -> link.setSourceAgent(agent)));

        destination.ifPresentOrElse(con -> {
            link.setDestinationConnection(con);
            link.setDestinationAgent(con.getAgent());
            link.setDestinationProcessName(con.getProcessName());
        }, () -> agentService.get(unknownAgentID).ifPresent(agent -> link.setDestinationAgent(agent)));


        return Optional.of(link);

    }


    @Override
    public void open(Connection connection) {
        Optional<ConnectionLink> potential = findLinks(connection);
        potential.ifPresentOrElse(
                link -> {
                    Optional<ConnectionLink> updated = updateLink(link, connection);
                    updated.ifPresent(update-> repository.save(update));
                    linkCache.invalidate(connection.getConnectionHash());
                },
                () -> {
                    Optional<ConnectionLink> link = createLink(connection);
                    link.ifPresent(connectionLink -> {
                        connectionLink = repository.save(connectionLink);
                        linkCache.put(connection.getConnectionHash(), connectionLink);
                    });
                }
        );
    }


    @Override
    public void close(Connection connection) {
        // I don't want to do anything with this yet.
    }

    @Override
    public List<ConnectionLink> findAll() {
        return repository.findAll();
    }

    @Override
    public void onApplicationEvent(NewOpenConnection newOpenConnection) {
        log.debug("processing new open connection");
        open(newOpenConnection.getConnection());
    }
}
