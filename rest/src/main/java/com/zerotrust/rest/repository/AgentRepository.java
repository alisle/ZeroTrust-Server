package com.zerotrust.rest.repository;

import com.zerotrust.model.entity.ViewAgent;
import com.zerotrust.model.entity.AgentCount;
import com.zerotrust.model.entity.ProcessCount;
import com.zerotrust.model.entity.UserCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

@SuppressWarnings("unused")
@PreAuthorize("hasAuthority('agents_read')")
@RepositoryRestResource(exported = true, path = "agents", collectionResourceRel = "agents", itemResourceRel = "agent")
public interface AgentRepository extends JpaRepository<ViewAgent, UUID> {

    @RestResource(exported = true)
    Page<ViewAgent> findAll(Pageable pageable);

    @RestResource(exported = true)
    Optional<ViewAgent> findById(UUID id);


    @RestResource(exported = true, path="total-agents", rel="totalAgents")
    long countByAliveIsTrueOrAliveIsFalse();

    @RestResource(exported = true, path="total-alive-agents", rel="totalAliveAgents")
    long countByAliveIsTrue();

    // We need to put these in a controller to get around the issue that Data REST has
    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.model.entity.UserCount(sourceUserName, COUNT(*)) FROM ViewConnectionLink WHERE sourceAgent.id = ?1 GROUP BY sourceUserName")
    List<UserCount> countSourceUsername(@Param("source_agent_id") UUID id);


    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.model.entity.UserCount(destinationUserName, COUNT(*)) FROM ViewConnectionLink WHERE destinationAgent.id = ?1 GROUP BY destinationUserName")
    List<UserCount> countDestinationUsername(@Param("destination_agent_id") UUID id);

    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.model.entity.UserCount(username, COUNT(*)) FROM Connection WHERE agent.id = ?1 GROUP BY username")
    List<UserCount> countUsername(@Param("agent_id") UUID id);

    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.model.entity.ProcessCount(sourceProcessName, COUNT(*)) FROM ViewConnectionLink WHERE sourceAgent.id = ?1 GROUP BY sourceProcessName")
    List<ProcessCount> countSourceProcess(@Param("source_agent_id") UUID id);

    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.model.entity.ProcessCount(destinationProcessName, COUNT(*)) FROM ViewConnectionLink WHERE destinationAgent.id = ?1 GROUP BY destinationProcessName")
    List<ProcessCount> countDestinationProcess(@Param("destination_agent_id") UUID id);

    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.model.entity.AgentCount((SELECT agent.name FROM ViewAgent  agent WHERE agent.id = connection.destinationAgent.id) as agent,  connection.destinationAgent.id as uuid, COUNT(*) as count) FROM ViewConnectionLink connection GROUP BY connection.destinationAgent")
    List<AgentCount> countIncomingConnections();

    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.model.entity.AgentCount((SELECT agent.name FROM ViewAgent agent WHERE agent.id = connection.sourceAgent.id) as agent,  connection.sourceAgent.id as uuid, COUNT(*) as count) FROM ViewConnectionLink connection GROUP BY connection.sourceAgent")
    List<AgentCount> countOutgoingConnections();

}
