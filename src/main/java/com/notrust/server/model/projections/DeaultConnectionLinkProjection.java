package com.notrust.server.model.projections;

import com.notrust.server.model.Agent;
import com.notrust.server.model.Connection;
import com.notrust.server.model.ConnectionLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;

@Projection(name="DefaultConnectionLinkProjection", types = {ConnectionLink.class})
public interface DeaultConnectionLinkProjection {
    Instant getTimestamp();
    long getConnectionHash();
    Agent getSourceAgent();
    Agent getDestinationAgent();
    Connection getSourceConnection();
    Connection getDestinationConnection();

    @Value("#{target.sourceConnection.alive}")
    boolean getAlive();
}
