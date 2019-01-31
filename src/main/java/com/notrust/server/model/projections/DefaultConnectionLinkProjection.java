package com.notrust.server.model.projections;

import com.notrust.server.model.Agent;
import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.util.UUID;

@Projection(name="DefaultConnectionLinkProjection", types = {ConnectionLink.class})
public interface DefaultConnectionLinkProjection {

    @Value("#{target.id}")
    UUID getId();

    Instant getTimestamp();
    long getConnectionHash();

    String getSourceAgentName();
    String getDestinationAgentName();

    Agent getSourceAgent();
    Agent getDestinationAgent();
    Connection getSourceConnection();
    Connection getDestinationConnection();

    boolean getAlive();
    String getSourceProcessName();
    String getDestinationProcessName();
}
