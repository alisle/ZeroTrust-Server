package com.notrust.server.model;

import com.google.errorprone.annotations.FormatMethod;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Value;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name="source_connection_id", nullable = false)
    private Connection sourceConnection;


    @NotNull
    @ManyToOne
    @JoinColumn(name="destination_connection_id", nullable = false)
    private Connection destinationConnection;

    // This is horrible, got to find a better way of doing this.
    @Formula("SELECT agent.name FROM agent agent WHERE agent.id =  source_agent_id")
    private String sourceAgentName;

    // Mate, this too is horrible.
    @Formula("SELECT agent.name FROM agent agent WHERE agent.id = destination_agent_id")
    private String destinationAgentName;

    // Really fucking horrible.
    @Formula("SELECT connection.connection_ended IS NULL FROM connection connection WHERE connection.id = source_connection_id")
    private boolean alive;

    // Christ, does it never end?!
    @Formula("SELECT connection.process_name FROM connection connection WHERE connection.id = source_connection_id")
    private String sourceProcessName;

    // Seriously lads, what's going on?
    @Formula("SELECT connection.process_name FROM connection connection WHERE connection.id = destination_connection_id")
    private String destinationProcessName;
}
