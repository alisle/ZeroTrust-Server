package com.zerotrust.links.repository;

import com.zerotrust.links.model.ConnectionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface ConnectionLinkRepository extends JpaRepository<ConnectionLink, UUID> {
    @Query("SELECT link from ConnectionLink  link where link.id = ?1")
    Optional<ConnectionLink> findById(UUID id);

    @Query("SELECT link FROM ConnectionLink link WHERE link.connectionHash = ?1 AND (link.sourceConnection IS NULL OR link.destinationConnection IS NULL)")
    ArrayList<ConnectionLink> findAllByOneSidedConnectionHash(long connectionHash);

    ConnectionLink findBySourceConnectionId(UUID connection);

    ConnectionLink findByDestinationConnectionId(UUID connection);

}
