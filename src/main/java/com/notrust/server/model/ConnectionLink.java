package com.notrust.server.model;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "connection_link")
public class ConnectionLink {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name = "connection_hash")
    private long connectionHash;

    @NotNull
    @Column(name = "created_on")
    private Instant timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name="source_agent_id", nullable = false)
    private Agent sourceAgent;

    @NotNull
    @ManyToOne
    @JoinColumn(name="destination_agent_id", nullable = false)
    private Agent destinationAgent;

    @ManyToOne
    @JoinColumn(name="source_connection_id", nullable = false)
    private Connection sourceConnection;

    @ManyToOne
    @JoinColumn(name="destination_connection_id", nullable = false)
    private Connection destinationConnection;

    // This is horrible, got to find a better way of doing this.
    @Formula("SELECT agent.name FROM agent agent WHERE agent.id =  source_agent_id")
    private String sourceAgentName;

    // Mate, this too is horrible.
    @Formula("SELECT agent.name FROM agent agent WHERE agent.id = destination_agent_id")
    private String destinationAgentName;


    @NotNull
    @Column(name = "alive")
    private boolean alive;


    @Column(name = "source_process_name")
    private String sourceProcessName;

    @Column(name = "destination_process_name")
    private String destinationProcessName;
}
