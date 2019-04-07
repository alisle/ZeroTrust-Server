package com.zerotrust.rest.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zerotrust.rest.events.NewCloseConnection;
import com.zerotrust.rest.events.NewOpenConnection;
import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.ConnectionLink;
import com.zerotrust.rest.model.IPAddress;
import com.zerotrust.rest.repository.ConnectionLinkRepository;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.service.ConnectionLinkService;
import com.zerotrust.rest.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ConnectionLinkServiceImpl implements ConnectionLinkService  {
    private final ConnectionLinkRepository repository;
    private final ConnectionService connectionService;
    private final AgentService agentService;

    @Value("${connection-link-service.use-cache}")
    private boolean useCache;

    private final Cache<Long, ConnectionLink> linkCache;

    public ConnectionLinkServiceImpl(ConnectionLinkRepository repository, ConnectionService connectionService, AgentService agentService) {
        this.repository = repository;
        this.connectionService = connectionService;
        this.agentService = agentService;
        this.linkCache = CacheBuilder.newBuilder().maximumSize(2048).build();
    }


    private Optional<ConnectionLink> findLinks(Connection connection) {
        if(useCache) {
            ConnectionLink potential = linkCache.getIfPresent(connection.getConnectionHash());
            if( potential != null) {
                log.debug("found matching link in cache");
                return Optional.of(potential);
            }
        }

        log.debug("unable to find link in cache, looking in the DB");
        List<ConnectionLink> potentials = repository.findAllByOneSidedConnectionHash(connection.getConnectionHash());
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

    private ConnectionLink populateLink(ConnectionLink link, Connection connection) {
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
            link.setSourceProcessName(con.getProcessName());
            link.setSourceAddress(con.getSource());
            link.setSourceString(con.getSourceString());
            link.setSourcePort(con.getSourcePort());
            link.setSourceUserName(con.getUsername());
            link.setSourceNetwork(con.getSourceNetwork());

            if(link.getDestinationConnection() == null) {
                link.setDestinationAddress(con.getDestination());
                link.setDestinationString(con.getDestinationString());
                link.setDestinationPort(con.getDestinationPort());
                link.setDestinationNetwork(con.getDestinationNetwork());
            }
        });

        destination.ifPresent(con -> {
            link.setDestinationConnection(con);
            link.setDestinationAgent(con.getAgent());
            link.setDestinationProcessName(con.getProcessName());
            link.setDestinationAddress(con.getDestination());
            link.setDestinationString(con.getDestinationString());
            link.setDestinationPort(con.getDestinationPort());
            link.setDestinationUserName(con.getUsername());
            link.setDestinationNetwork(con.getDestinationNetwork())
            ;
            if(link.getSourceConnection() == null) {
                link.setSourceAddress(con.getSource());
                link.setSourceString(con.getSourceString());
                link.setSourcePort(con.getSourcePort());
                link.setSourceNetwork(con.getSourceNetwork());
            }
        });

        return link;
    }

    private Optional<ConnectionLink> createLink(Connection connection) {
        ConnectionLink link = new ConnectionLink();
        link.setTimestamp(connection.getStart());
        link.setId(UUID.randomUUID());
        link.setConnectionHash(connection.getConnectionHash());
        link.setAlive(true);

        link = populateLink(link, connection);
        if(link.getDestinationAgent() == null && link.getSourceAgent() == null) {
            return Optional.empty();
        }

        return Optional.of(link);

    }


    @Override
    public void open(Connection connection) {
        Optional<ConnectionLink> potential = findLinks(connection);
        potential.ifPresentOrElse(
                link -> {
                    ConnectionLink updated = populateLink(link, connection);
                    repository.save(updated);
                    if(useCache) {
                        linkCache.invalidate(connection.getConnectionHash());
                    }
                },
                () -> {
                    Optional<ConnectionLink> link = createLink(connection);
                    link.ifPresent(connectionLink -> {
                        connectionLink = repository.save(connectionLink);
                        if(log.isDebugEnabled()) {
                            log.debug("created new link: " + connectionLink.getId() + " with connection: " + connection.getId());
                        }
                        if(useCache) {
                            linkCache.put(connection.getConnectionHash(), connectionLink);
                        }
                    });
                }
        );
    }


    @Override
    public void close(Connection connection) {
        ConnectionLink link = repository.findBySourceConnectionId(connection.getId());
        if(link == null) {
            link = repository.findByDestinationConnectionId(connection.getId());
        }

        if(link == null) {
            log.error("can not find a corresponding connection link to connection " + connection.getId());
            return;
        }

        link.setAlive(false);
        link.setEnded(connection.getEnd());
        link.setDuration(link.getEnded().getEpochSecond() - link.getTimestamp().getEpochSecond());

        repository.save(link);

    }

    @Override
    public List<ConnectionLink> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ConnectionLink> get(UUID id) {
        return repository.findById(id);
    }

    @EventListener
    public void onApplicationEvent(NewOpenConnection newOpenConnection) {
        log.debug("processing new open connection");
        open(newOpenConnection.getConnection());
    }

    @EventListener
    public void onApplicationEvent(NewCloseConnection newCloseConnection) {
        log.debug("processing new close connection");
        close(newCloseConnection.getConnection());
    }

}
