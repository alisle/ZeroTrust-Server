package com.zerotrust.rest.repository;

import com.zerotrust.model.entity.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    @RestResource(exported = true)
    Page<Connection> findAll(Pageable pageable);

    @RestResource(exported = false)
    ArrayList<Connection> findAll();

    @RestResource(exported = true)
    Optional<Connection> findById(UUID id);

    @RestResource(exported = false)
    List<Connection> findByConnectionHash(long connectionHash);

    @RestResource(exported = false)
    List<Connection> findByAliveIsTrueAndAgentId(UUID agent);


}