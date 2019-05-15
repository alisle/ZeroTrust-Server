package com.zerotrust.rest.projections;

import com.zerotrust.rest.model.ViewAgent;
import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.ViewConnectionLink;
import com.zerotrust.rest.model.Network;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.util.UUID;

@Projection(name="DefaultConnectionLinkProjection", types = {ViewConnectionLink.class})
public interface DefaultConnectionLinkProjection {

    @Value("#{target.id}")
    UUID getUUID();

    long getConnectionHash();
    Instant getTimestamp();
    Instant getEnded();
    long getDuration();

    String getSourceAgentName();
    ViewAgent  getSourceAgent();
    String getSourceString();
    int getSourceAddress();
    int getSourcePort();
    Connection getSourceConnection();
    Network getSourceNetwork();
    String getSourceNetworkName();

    String getDestinationAgentName();
    ViewAgent  getDestinationAgent();
    Connection getDestinationConnection();
    Network getDestinationNetwork();
    String getDestinationNetworkName();

    String getDestinationString();
    int getDestinationAddress();
    int getDestinationPort();


    boolean getAlive();
    String getSourceProcessName();
    String getDestinationProcessName();


}
