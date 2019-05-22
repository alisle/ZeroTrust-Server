package com.zerotrust.rest.projections;

import com.zerotrust.model.entity.ViewAgent;
import com.zerotrust.model.entity.Connection;
import com.zerotrust.model.entity.ViewConnectionLink;
import com.zerotrust.model.entity.Network;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.util.UUID;

@Projection(name="defaultConnectionLinkProjection", types = {ViewConnectionLink.class})
public interface DefaultConnectionLinkProjection {

    @Value("#{target.id}")
    UUID getUUID();

    long getConnectionHash();
    Instant getTimestamp();
    Instant getEnded();
    long getDuration();

    String getSourceAgentName();

    @Value("#{target.sourceAgent}")
    ViewAgent  getSourceAgent();

    String getSourceString();
    int getSourceAddress();
    int getSourcePort();
    Connection getSourceConnection();
    Network getSourceNetwork();
    String getSourceNetworkName();

    String getDestinationAgentName();

    @Value("#{target.destinationAgent}")
    ViewAgent getDestinationAgent();

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
