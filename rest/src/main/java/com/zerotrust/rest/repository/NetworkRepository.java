package com.zerotrust.rest.repository;

import com.zerotrust.model.entity.AgentCount;
import com.zerotrust.model.entity.Network;
import com.zerotrust.rest.projections.DefaultNetworkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")

@PreAuthorize("hasAuthority('networks_read')")
@RepositoryRestResource(exported = true,
        path = "networks",
        collectionResourceRel = "networks",
        collectionResourceDescription = @Description("Networks defined with ZeroTrust"),
        itemResourceRel = "network",
        itemResourceDescription = @Description("A Network is a group of agents / IPs defined by their CIDR / Subnet Mask"),
        excerptProjection = DefaultNetworkProjection.class
)
public interface  NetworkRepository extends JpaRepository<Network, UUID> {

    @RestResource(exported = true)
    Page<Network> findAll(Pageable pageable);


    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.model.entity.AgentCount((SELECT agent.name FROM ViewAgent agent WHERE agent.id = connection.destinationAgent.id) as agent,  connection.destinationAgent.id as uuid, COUNT(*) as count) FROM ViewConnectionLink connection WHERE connection.alive = TRUE AND connection.destinationNetwork.id = ?1 GROUP BY connection.destinationAgent")
    List<AgentCount> activeDestinationConnections(@Param("network_id") UUID network);


    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.model.entity.AgentCount((SELECT agent.name FROM ViewAgent agent WHERE agent.id = connection.sourceAgent.id) as agent,  connection.sourceAgent.id as uuid, COUNT(*) as count) FROM ViewConnectionLink connection WHERE connection.alive = TRUE AND connection.sourceNetwork.id = ?1 GROUP BY connection.sourceAgent")
    List<AgentCount> activeSourceConnections(@Param("network_id") UUID network);


}
