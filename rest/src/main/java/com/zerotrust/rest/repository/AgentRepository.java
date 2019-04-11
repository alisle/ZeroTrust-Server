package com.zerotrust.rest.repository;

import com.zerotrust.rest.model.Agent;
import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.ProcessCount;
import com.zerotrust.rest.model.UserCount;
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
@RepositoryRestResource(exported = true, path = "agents", collectionResourceRel = "agents", itemResourceRel = "agent")
public interface AgentRepository extends JpaRepository<Agent, UUID> {
    @RestResource(exported = true)
    Page<Agent> findAll(Pageable pageable);

    @RestResource(path = "connection", exported = true)
    Page<Agent> findAllOrderByConnections(Pageable pageable);


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

    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.rest.model.UserCount(sourceUserName, COUNT(*)) FROM ConnectionLink WHERE sourceAgent.id = ?1 GROUP BY sourceUserName")
    List<UserCount> countSourceUsername(@Param("source_agent_id") UUID id);


    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.rest.model.UserCount(destinationUserName, COUNT(*)) FROM ConnectionLink WHERE destinationAgent.id = ?1 GROUP BY destinationUserName")
    List<UserCount> countDestinationUsername(@Param("destination_agent_id") UUID id);

    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.rest.model.UserCount(username, COUNT(*)) FROM Connection WHERE agent.id = ?1 GROUP BY username")
    List<UserCount> countUsername(@Param("agent_id") UUID id);


    @RestResource(exported = false)
    @Query("SELECT new com.zerotrust.rest.model.ProcessCount(sourceProcessName, COUNT(*)) FROM ConnectionLink WHERE sourceAgent.id = ?1 GROUP BY sourceProcessName")
    List<ProcessCount> countSourceProcess(@Param("source_agent_id") UUID id);

    @RestResource(exported = false)
    @Query(value = "SELECT new com.zerotrust.rest.model.ProcessCount(destinationProcessName, COUNT(*)) FROM ConnectionLink WHERE destinationAgent.id = ?1 GROUP BY destinationProcessName")
    List<ProcessCount> countDestinationProcess(@Param("destination_agent_id") UUID id);

    @RestResource(exported = false, path="count-incoming-connections", rel="countIncomingConnections")
    @Query(value = "SELECT new com.zerotrust.rest.model.AgentCount((SELECT agent.name FROM Agent agent WHERE agent.id = connection.destinationAgent.id) as agent,  connection.destinationAgent.id as uuid, COUNT(*) as count) FROM ConnectionLink connection GROUP BY connection.destinationAgent")
    List<AgentCount> countIncomingConnections();

    @RestResource(exported = false, path="count-outgoing-connections", rel="countOutgoingConnections")
    @Query(value = "SELECT new com.zerotrust.rest.model.AgentCount((SELECT agent.name FROM Agent agent WHERE agent.id = connection.sourceAgent.id) as agent,  connection.sourceAgent.id as uuid, COUNT(*) as count) FROM ConnectionLink connection GROUP BY connection.sourceAgent")
    List<AgentCount> countOutgoingConnections();



    @RestResource(exported = true, path = "total-agents", rel = "totalAgents")
    long countByAliveIsTrueOrAliveIsFalse();

    @RestResource(exported = true, path = "total-alive-agents", rel = "totalAliveAgents")
    long countByAliveIsTrue();
}
