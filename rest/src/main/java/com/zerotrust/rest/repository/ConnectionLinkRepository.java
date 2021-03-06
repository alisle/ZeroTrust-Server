package com.zerotrust.rest.repository;

import com.zerotrust.model.entity.ViewConnectionLink;
import com.zerotrust.rest.projections.DefaultConnectionLinkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@PreAuthorize("hasAuthority('connections_read')")
@RepositoryRestResource(exported = true,
        path = "connection_links",
        collectionResourceRel = "connection_links",
        collectionResourceDescription = @Description("Connection Links between hosts in the ZeroTrust network"),
        itemResourceRel = "connection_link",
        itemResourceDescription = @Description("Link between 2 hosts"),
        excerptProjection = DefaultConnectionLinkProjection.class
)
public interface ConnectionLinkRepository extends JpaRepository<ViewConnectionLink, UUID> {

    @RestResource(exported = true)
    Page<ViewConnectionLink> findAll(Pageable pageable);

    @RestResource(exported = false)
    ArrayList<ViewConnectionLink> findAll();

    @RestResource(exported = true)
    @Query("SELECT link from ViewConnectionLink  link where link.id = ?1")
    Optional<ViewConnectionLink> findById(UUID id);

    @RestResource(exported = true)
    Page<ViewConnectionLink> findAllByConnectionHash(Pageable pageable, @Param("hash") long ConnectionHash);

    @RestResource(exported = false)
    @Query("SELECT link FROM ViewConnectionLink link WHERE link.connectionHash = ?1 AND (link.sourceConnection IS NULL OR link.destinationConnection IS NULL)")
    ArrayList<ViewConnectionLink> findAllByOneSidedConnectionHash(long connectionHash);

    @RestResource(exported = true, path="agent-connections", rel="findByAgentID")
    @Query("SELECT link from ViewConnectionLink link WHERE link.sourceAgent.id = ?1 OR link.destinationAgent.id = ?1")
    Page<ViewConnectionLink> findAllByAgentId(Pageable pageable, @Param("agent-id") UUID agent);

    @RestResource(exported = true, path="source-destination-agent-id", rel = "findBySourceAndDestinationAgentID")
    Page<ViewConnectionLink> findAllBySourceAgentIdAndDestinationAgentId(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true, path="source-agent-id-destination-ip", rel = "findBySourceAgentIDAndDestinationIP")
    Page<ViewConnectionLink> findAllBySourceAgentIdAndDestinationString(Pageable pageable, @Param("source_agent_id") UUID source, @Param("destination_address") String address);

    @RestResource(exported = true, path="source-ip-destination-agent-id", rel= "findBySourceIPAndDestinationAgentID")
    Page<ViewConnectionLink> findAllBySourceStringAndDestinationAgentId(Pageable pageable, @Param("source_address") String address, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true, path="active-source-agent-id", rel = "findActiveBySourceAgentID")
    Page<ViewConnectionLink> findAllBySourceAgentIdAndAliveTrue(Pageable pageable, @Param("source_agent_id") UUID source);

    @RestResource(exported = true, path="active-destination-agent-id", rel="findActiveByDestinationAgentID")
    Page<ViewConnectionLink> findAllByDestinationAgentIdAndAliveTrue(Pageable pageable, @Param("destination_agent_id") UUID destination);

    @RestResource(exported = true, path="active", rel = "findActive")
    Page<ViewConnectionLink> findAllByAliveIsTrue(Pageable pageable);

    @RestResource(exported = true, path = "total-connection-links", rel = "totalConnectionLinks")
    long countByAliveIsTrueOrAliveIsFalse();

    @RestResource(exported = true, path = "total-alive-connection-links", rel = "totalAliveConnectionLinks")
    long countByAliveIsTrue();
}
