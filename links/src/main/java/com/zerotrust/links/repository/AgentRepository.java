package com.zerotrust.links.repository;

import com.zerotrust.links.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface AgentRepository  extends JpaRepository<Agent, UUID> {

}
