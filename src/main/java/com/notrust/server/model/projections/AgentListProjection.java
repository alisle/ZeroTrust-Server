package com.notrust.server.model.projections;

import com.notrust.server.model.Agent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.util.UUID;

@Projection(name = "agentList", types = { Agent.class })
public interface AgentListProjection {
    @Value("#{target.id}")
    UUID getId();

    String getName();
    Instant getRegisteredAt();
    Instant getFirstSeen();
    Instant getLastSeen();
    Boolean getAlive();
    Boolean getKnown();

    @Value("#{target.getConnections().size()}")
    long getConnectionCount();
}
