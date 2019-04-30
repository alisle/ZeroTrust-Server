package com.zerotrust.rest.repository;

import com.zerotrust.rest.model.ConnectionLink;
import com.zerotrust.rest.model.projections.DefaultConnectionLinkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false, path = "connection_links", collectionResourceRel = "connection_links", itemResourceRel = "connection_link", excerptProjection = DefaultConnectionLinkProjection.class)
public interface ConnectionLinkRepository extends JpaRepository<ConnectionLink, UUID> {

    Page<ConnectionLink> findAll(Pageable pageable);
    ArrayList<ConnectionLink> findAll();

    @Query("SELECT link from ConnectionLink  link where link.id = ?1")
    Optional<ConnectionLink> findById(UUID id);

    Page<ConnectionLink> findAllByConnectionHash(Pageable pageable, @Param("hash") long ConnectionHash);

    @Query("SELECT link FROM ConnectionLink link WHERE link.connectionHash = ?1 AND (link.sourceConnection IS NULL OR link.destinationConnection IS NULL)")
    ArrayList<ConnectionLink> findAllByOneSidedConnectionHash(long connectionHash);

    @Query("SELECT link from ConnectionLink link WHERE link.sourceAgent.id = ?1 OR link.destinationAgent.id = ?1")
    Page<ConnectionLink> findAllByAgentId(Pageable pageable, @Param("agent-id") UUID agent);

    Page<ConnectionLink> findAllBySourceAgentIdAndDestinationAgentId(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_agent_id") UUID destination);

    Page<ConnectionLink> findAllBySourceAgentIdAndDestinationString(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_address") String address);

    Page<ConnectionLink> findAllBySourceStringAndDestinationAgentId(Pageable pageable, @Param("source_address") String address, @Param("destination_agent_id") UUID destination);

    ConnectionLink findBySourceConnectionId(UUID connection);

    ConnectionLink findByDestinationConnectionId(UUID connection);

    Page<ConnectionLink> findAllBySourceAgentIdAndAliveTrue(Pageable pageable, @Param("source_agent_id") UUID source);

    Page<ConnectionLink> findAllByDestinationAgentIdAndAliveTrue(Pageable pageable, @Param("destination_agent_id") UUID destination);

    Page<ConnectionLink> findAllByAliveIsTrue(Pageable pageable);

    long countByAliveIsTrueOrAliveIsFalse();

    long countByAliveIsTrue();
}
