package com.zerotrust.server.model.projections;

import com.zerotrust.server.model.Agent;
import com.zerotrust.server.model.Network;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;
import java.util.UUID;

@Projection(name = "DefaultNetworkProjection", types= {Network.class})
public interface DefaultNetworkProjection {
    @Value("#{target.id}")
    UUID getUUID();

    String getName();
    String getDescription();
    String getAddressString();
    String getMaskString();

    @Value("#{target.agents.size()}")
    int getAgentNumber();

    long getSourceConnectionCount();
    long getDestinationConnectionCount();

    long getActiveSourceConnectionCount();
    long getActiveDestinationConnectionCount();
}
