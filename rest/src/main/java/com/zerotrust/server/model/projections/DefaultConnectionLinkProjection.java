package com.zerotrust.server.model.projections;

import com.zerotrust.server.model.Agent;
import com.zerotrust.server.model.Connection;
import com.zerotrust.server.model.ConnectionLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.util.UUID;

@Projection(name="DefaultConnectionLinkProjection", types = {ConnectionLink.class})
public interface DefaultConnectionLinkProjection {

    @Value("#{target.id}")
    UUID getUUID();

    long getConnectionHash();
    Instant getTimestamp();
    Instant getEnded();
    long getDuration();

    String getSourceAgentName();
    Agent  getSourceAgent();
    String getSourceString();
    int getSourceAddress();
    int getSourcePort();
    Connection getSourceConnection();

    String getDestinationAgentName();
    Agent  getDestinationAgent();
    Connection getDestinationConnection();
    String getDestinationString();
    int getDestinationAddress();
    int getDestinationPort();


    boolean getAlive();
    String getSourceProcessName();
    String getDestinationProcessName();


}
