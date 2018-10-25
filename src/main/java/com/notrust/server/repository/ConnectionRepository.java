package com.notrust.server.repository;

import com.notrust.server.model.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = true, path = "connections", collectionResourceRel = "connections", itemResourceRel = "connection")
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    @RestResource(exported = true)
    Page<Connection> findAll(Pageable pageable);

    @RestResource(exported = true)
    Optional<Connection> findById(UUID id);

}
