package com.notrust.server.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.notrust.server.events.NewOpenConnection;
import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import com.notrust.server.model.IPAddress;
import com.notrust.server.repository.ConnectionLinkRepository;
import com.notrust.server.service.ConnectionLinkService;
import com.notrust.server.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionLinkServiceImpl implements ConnectionLinkService, ApplicationListener<NewOpenConnection> {
    private final ConnectionLinkRepository repository;
    private final ConnectionService connectionService;
    private final Cache<Long, Connection> cache;

    public ConnectionLinkServiceImpl(ConnectionLinkRepository repository, ConnectionService connectionService) {
        this.repository = repository;
        this.connectionService = connectionService;
        this.cache = CacheBuilder.newBuilder().maximumSize(100).build();
    }

    private Optional<Connection> findCompanions(Connection connection) {
        Connection potential = cache.getIfPresent(connection.getConnectionHash());
        if( potential != null) {
            log.debug("found matching connection in cache, returning");
            return Optional.of(potential);
        }

        log.debug("looking in the DB for possible connections");
        List<Connection> potentials = connectionService.findConnectionHash(connection.getConnectionHash());

        Optional<Connection> best = Optional.empty();
        for (Connection conn: potentials) {
            if(conn.isAlive() && connection.getId() != conn.getId()) {
                if(best.orElse(conn).getStart().isBefore(conn.getStart())) {
                    best = Optional.of(conn);
                }
            }
        }

        return best;
    }

    private Optional<ConnectionLink> populateLink(Connection first, Connection second) {
        Optional<Connection> source = Optional.empty();
        Optional<Connection> destination = Optional.empty();

        final ConnectionLink link = new ConnectionLink();

        if(!first.getSourceString().equals(second.getSourceString())) {
            log.error("sourceString addresses aren't the same, this really should NOT happen!");
        }

        if(!first.getDestinationString().equals(second.getDestinationString())) {
            log.error("destinationString addresses aren't the same, this really should NOT happen!");
        }

        IPAddress sourceAddress = new IPAddress(first.getSource(), first.getSourceString(), IPAddress.Version.V4);
        IPAddress destinationAddress = new IPAddress(first.getDestination(), first.getDestinationString(), IPAddress.Version.V4);

        Set<IPAddress> firstAgentAddresses  = first.getAgent().getAddresses();
        Set<IPAddress> secondAgentAddresses = second.getAgent().getAddresses();

        if(firstAgentAddresses.contains(sourceAddress)) {
            source = Optional.of(first);
        } else if (secondAgentAddresses.contains(sourceAddress)) {
            source = Optional.of(second);
        } else {
            log.error("unable to map source ip to a particular agents");
        }

        if(firstAgentAddresses.contains(destinationAddress)) {
            destination = Optional.of(first);
        } else if (secondAgentAddresses.contains(destinationAddress)) {
            destination = Optional.of(second);
        }

        if(destination.isPresent() && source.isPresent()) {
            link.setId(UUID.randomUUID());
            link.setConnectionHash(first.getConnectionHash());
            link.setDestinationAgent(destination.get().getAgent());
            link.setSourceAgent(source.get().getAgent());
            link.setSourceConnection(source.get());
            link.setDestinationConnection(destination.get());
            link.setTimestamp(source.get().getStart());

            return Optional.ofNullable(link);
        }

        return Optional.empty();
    }

    @Override
    public void open(Connection connection) {
        Optional<Connection> potential = findCompanions(connection);
        potential.ifPresentOrElse(
                companion -> {
                        Optional<ConnectionLink> possible = populateLink(connection, companion);
                        possible.ifPresent(link -> repository.save(link));
                    },
                () -> cache.put(connection.getConnectionHash(), connection)
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
