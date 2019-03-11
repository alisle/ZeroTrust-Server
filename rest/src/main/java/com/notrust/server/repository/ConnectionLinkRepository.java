package com.notrust.server.repository;

import com.notrust.server.model.ConnectionLink;
import com.notrust.server.model.projections.DefaultConnectionLinkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = true, path = "connection_links", collectionResourceRel = "connection_links", itemResourceRel = "connection_link", excerptProjection = DefaultConnectionLinkProjection.class)
public interface ConnectionLinkRepository extends JpaRepository<ConnectionLink, UUID> {
    @RestResource(exported = true)
    Page<ConnectionLink> findAllBy(Pageable pageable);

    @RestResource(exported = false)
    ArrayList<ConnectionLink> findAll();

    @RestResource(exported = true)
    @Query("SELECT link from ConnectionLink  link where link.id = ?1")
    Optional<ConnectionLink> findById(UUID id);

    @RestResource(exported = true)
    Page<ConnectionLink> findAllByConnectionHash(Pageable pageable, @Param("hash") long ConnectionHash);

    @RestResource(exported = false)
    @Query("SELECT link FROM ConnectionLink link WHERE link.connectionHash = ?1 AND (link.sourceConnection IS NULL OR link.destinationConnection IS NULL)")
    ArrayList<ConnectionLink> findAllByOneSidedConnectionHash(long connectionHash);

    @RestResource(exported = true, path="agent-connections", rel="findByAgentID")
    @Query("SELECT link from ConnectionLink link WHERE link.sourceAgent.id = ?1 OR link.destinationAgent.id = ?1")
    Page<ConnectionLink> findAllByAgentId(Pageable pageable, @Param("agent-id") UUID agent);

    @RestResource(exported = true, path="source-destination-agent-id", rel = "findBySourceAndDestinationAgentID")
    Page<ConnectionLink> findAllBySourceAgentIdAndDestinationAgentId(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true, path="source-agent-id-destination-ip", rel = "findBySourceAgentIDAndDestinationIP")
    Page<ConnectionLink> findAllBySourceAgentIdAndDestinationString(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_address") String address);

    @RestResource(exported = true, path="source-ip-destination-agent-id", rel= "findBySourceIPAndDestinationAgentID")
    Page<ConnectionLink> findAllBySourceStringAndDestinationAgentId(Pageable pageable, @Param("source_address") String address, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true)
    ConnectionLink findBySourceConnectionId(@Param("source_connection_id") UUID connection);

    @RestResource(exported = true)
    ConnectionLink findByDestinationConnectionId(@Param("destination_connection_id") UUID connection);

    @RestResource(exported = true, path="active-source-agent-id", rel = "findActiveBySourceAgentID")
    Page<ConnectionLink> findAllBySourceAgentIdAndAliveTrue(Pageable pageable, @Param("source_agent_id") UUID source);

    @RestResource(exported = true, path="active-destination-agent-id", rel="findActiveByDestinationAgentID")
    Page<ConnectionLink> findAllByDestinationAgentIdAndAliveTrue(Pageable pageable, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true, path="active", rel = "findActive")
    Page<ConnectionLink> findAllByAliveIsTrue(Pageable pageable);

    @RestResource(exported = true, path = "total-connection-links", rel = "totalConnectionLinks")
    long countByAliveIsTrueOrAliveIsFalse();

    @RestResource(exported = true, path = "total-alive-connection-links", rel = "totalAliveConnectionLinks")
    long countByAliveIsTrue();

}
