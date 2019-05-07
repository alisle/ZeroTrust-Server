package com.zerotrust.links.repository;

import com.zerotrust.links.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    List<Connection> findByAliveIsTrueAndAgentId(UUID agent);
}
