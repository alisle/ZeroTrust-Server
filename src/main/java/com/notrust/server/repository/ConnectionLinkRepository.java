package com.notrust.server.repository;

import com.notrust.server.model.ConnectionLink;
import com.notrust.server.model.projections.DefaultConnectionLinkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
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
    Optional<ConnectionLink> findById(UUID id);

    @RestResource(exported = true)
    Page<ConnectionLink> findAllByConnectionHash(Pageable pageable, @Param("hash") long ConnectionHash);
}
