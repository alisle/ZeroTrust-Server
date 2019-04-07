package com.zerotrust.rest.repository;

import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.Network;
import com.zerotrust.rest.model.projections.DefaultNetworkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = true, path = "networks", collectionResourceRel = "networks", itemResourceRel = "network", excerptProjection = DefaultNetworkProjection.class)
public interface NetworkRepository extends JpaRepository<Network, UUID> {

    @RestResource(exported = true)
    Page<Network> findAll(Pageable pageable);

    @RestResource(exported = false)
    List<Network> findAll();

    @RestResource(exported = false)
    List<Network> findByAddress(int address);

    @RestResource(exported=false, path="count-active-destination-connections", rel="countActiveDestinationConnections")
    @Query(value = "SELECT new com.zerotrust.rest.model.AgentCount((SELECT agent.name FROM Agent agent WHERE agent.id = connection.destinationAgent.id) as agent,  connection.destinationAgent.id as uuid, COUNT(*) as count) FROM ConnectionLink connection WHERE connection.alive = TRUE AND connection.destinationNetwork.id = ?1 GROUP BY connection.destinationAgent")
    List<AgentCount> activeDestinationConnections(@Param("id") UUID network);

    @RestResource(exported=false, path="count-active-source-connections", rel="countActiveSourceConnections")
    @Query(value = "SELECT new com.zerotrust.rest.model.AgentCount((SELECT agent.name FROM Agent agent WHERE agent.id = connection.sourceAgent.id) as agent,  connection.sourceAgent.id as uuid, COUNT(*) as count) FROM ConnectionLink connection WHERE connection.alive = TRUE AND connection.sourceNetwork.id = ?1 GROUP BY connection.sourceAgent")
    List<AgentCount> activeSourceConnections(@Param("id") UUID network);

}
