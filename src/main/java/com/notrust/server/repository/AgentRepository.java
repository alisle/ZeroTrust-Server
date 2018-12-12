package com.notrust.server.repository;

import com.notrust.server.model.Agent;
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
@RepositoryRestResource(exported = true, path = "agents", collectionResourceRel = "agents", itemResourceRel = "agent")
public interface AgentRepository extends JpaRepository<Agent, UUID> {
    @RestResource(exported = true)
    Page<Agent> findAll(Pageable pageable);

    @RestResource(exported = false)
    ArrayList<Agent> findAll();

    @RestResource(exported = true)
    Optional<Agent> findById(UUID id);

    @RestResource(path = "known", rel = "known")
    Page<Agent> findAllByKnown(@Param("known") boolean known, Pageable pageable);

    @RestResource(exported = false)
    ArrayList<Agent> findAllByKnown(boolean known);


    @RestResource(path = "alive", rel = "alive")
    Page<Agent> findAllByAlive(@Param("alive") boolean alive, Pageable pageable);

    @RestResource(exported = false)
    ArrayList<Agent> findAllByAlive(boolean agent);

}